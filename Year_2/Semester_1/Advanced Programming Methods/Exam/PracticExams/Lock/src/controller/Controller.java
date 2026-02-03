package controller;

import exceptions.InterpreterException;
import exceptions.MyException;
import model.state.*;
import model.statement.IStatement;
import model.value.IValue;
import model.value.RefValue;
import repository.IRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller implements IController {

    private ExecutorService executor;
    private final IRepository repository;
    private final boolean useUnsafeGC;

    public Controller(IRepository repo, boolean useUnsafeGC) {
        this.repository = repo;
        this.useUnsafeGC = useUnsafeGC;
    }

    public Controller(IRepository repository) {
        this(repository, false);
    }

    //---------->GC HELPERS

    private List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> ((RefValue) v).getAddr())
                .toList();
    }

    private Map<Integer, IValue> unsafeGarbageCollector(
            List<Integer> symTableAddr,
            Map<Integer, IValue> heap) {

        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Set<Integer> getReachableAddresses(
            Set<Integer> roots,
            Map<Integer, IValue> heap) {

        Set<Integer> reachable = new HashSet<>(roots);
        Deque<Integer> work = new ArrayDeque<>(roots);

        while (!work.isEmpty()) {
            int addr = work.pop();

            if (heap.containsKey(addr)) {
                IValue val = heap.get(addr);

                if (val instanceof RefValue ref) {
                    int next = ref.getAddr();
                    if (next != 0 && reachable.add(next)) {
                        work.push(next);
                    }
                }
            }
        }

        return reachable;
    }

    private Map<Integer, IValue> safeGarbageCollector(
            Set<Integer> reachable,
            Map<Integer, IValue> heap) {

        return heap.entrySet().stream()
                .filter(e -> reachable.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    //---------->REMOVE COMPLETED

    @Override
    public List<ProgramState> removeCompletedPrg(List<ProgramState> inPrgList) {
        return inPrgList.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    //---------->ONE STEP FOR ALL PROGRAMS

    @Override
    public void oneStepForAllPrg(List<ProgramState> prgList) throws MyException, InterruptedException {

        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(2);
        }

        // Log inainte de executie
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (Exception ignored) {}
        });

        // pregatesc lista de callables
        List<Callable<ProgramState>> callList = prgList.stream()
                .map((ProgramState p) -> (Callable<ProgramState>) p::oneStep)
                .collect(Collectors.toList());

        // executam in paralel: poate aparea un nou PrgState datorita fork()
        List<ProgramState> newPrgList =
                executor.invokeAll(callList).stream()
                        .map(future -> {
                            try {
                                return future.get();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        // adaug firele noi
        prgList.addAll(newPrgList);

        // Log după executie
        prgList.forEach(prg -> {
            try {
                repository.logPrgStateExec(prg);
            } catch (Exception ignored) {}
        });

        // actualizez repository
        repository.setPrgList(prgList);

        // ✅ opțional: dacă nu mai există programe active, închide executorul
        if (removeCompletedPrg(prgList).isEmpty()) {
            executor.shutdownNow();
        }
    }

    //----------> ALL STEP (MULTITHREADED)

    @Override
    public void allStep() throws MyException, InterpreterException, IOException {

        executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgList = removeCompletedPrg(repository.getPrgList());

        while (!prgList.isEmpty()) {

            // GARBAGE COLLECTOR
            Map<Integer, IValue> newHeap;

            if (useUnsafeGC) {
                // UNSAFE mode (only direct references)
                List<Integer> roots =
                        prgList.stream()
                                .flatMap(p ->
                                        getAddrFromSymTable(
                                                p.getSymbolTable().getContent().values()
                                        ).stream())
                                .toList();

                newHeap = unsafeGarbageCollector(
                        roots,
                        prgList.get(0).getHeap().getContent()
                );

            } else {
                // SAFE mode (reachable references)
                List<Integer> roots =
                        prgList.stream()
                                .flatMap(p ->
                                        getAddrFromSymTable(
                                                p.getSymbolTable().getContent().values()
                                        ).stream())
                                .toList();

                Set<Integer> reachable =
                        getReachableAddresses(
                                new HashSet<>(roots),
                                prgList.get(0).getHeap().getContent()
                        );

                newHeap = safeGarbageCollector(
                        reachable,
                        prgList.get(0).getHeap().getContent()
                );
            }

            // aplic GC
            prgList.get(0).getHeap().setContent(newHeap);

            // ---------->ONE PARALLEL STEP
            try {
                oneStepForAllPrg(prgList);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // elimin programele terminate
            prgList = removeCompletedPrg(repository.getPrgList());
        }

        executor.shutdownNow();

        repository.setPrgList(prgList);
    }

    @Override
    public ProgramState copyProgramState(ProgramState original) throws MyException {
        IStatement copied = original.getOriginalProgram().deepCopy();
        return new ProgramState(
                new StackExecutionStack<>(),
                new MapSymbolTable<>(),
                new ListOut<>(),
                copied,
                new MapFileTable()
        );
    }

    @Override
    public List<ProgramState> getProgramStates() {
        return repository.getPrgList();
    }

    @Override
    public void setProgramStates(List<ProgramState> prgList) {
        repository.setPrgList(prgList);
    }
}
