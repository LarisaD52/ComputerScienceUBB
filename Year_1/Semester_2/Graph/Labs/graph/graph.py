import copy

class Graph:
    def __init__(self, number_of_vertices, number_of_edges):
        self.__number_of_vertices = number_of_vertices
        self.__number_of_edges = number_of_edges

        self.__incoming_edges = {}
        self.__outgoing_edges = {}
        self.__costs = {}

        for i in range(number_of_vertices):
            self.__incoming_edges[i] = []
            self.__outgoing_edges[i] = []

    # ----------------- BASIC GETTERS -----------------

    @property
    def number_of_vertices(self):
        return self.__number_of_vertices

    @property
    def number_of_edges(self):
        return self.__number_of_edges

    @property
    def incoming_edges(self):
        return self.__incoming_edges

    @property
    def outgoing_edges(self):
        return self.__outgoing_edges

    @property
    def costs(self):
        return self.__costs

    # ----------------- BASIC PARSERS -----------------

    def parse_vertices(self):
        for v in list(self.__outgoing_edges.keys()):
            yield v

    def parse_inbound_edges(self, v):
        for x in self.__incoming_edges[v]:
            yield x

    def parse_outbound_edges(self, v):
        for x in self.__outgoing_edges[v]:
            yield x

    def parse_cost(self):
        for key in list(self.__costs.keys()):
            yield key

    # ----------------- BASIC OPERATIONS -----------------

    def add_vertex(self, v):
        if v in self.__incoming_edges:
            return False
        self.__incoming_edges[v] = []
        self.__outgoing_edges[v] = []
        self.__number_of_vertices += 1
        return True

    def remove_vertex(self, v):
        if v not in self.__incoming_edges:
            return False

        for x in list(self.__outgoing_edges.keys()):
            if v in self.__outgoing_edges[x]:
                self.__outgoing_edges[x].remove(v)

        for x in list(self.__incoming_edges.keys()):
            if v in self.__incoming_edges[x]:
                self.__incoming_edges[x].remove(v)

        self.__incoming_edges.pop(v)
        self.__outgoing_edges.pop(v)

        for (x, y) in list(self.__costs.keys()):
            if x == v or y == v:
                self.__costs.pop((x, y))
                self.__number_of_edges -= 1

        self.__number_of_vertices -= 1
        return True

    def add_edge(self, x, y, cost):
        if x not in self.__outgoing_edges or y not in self.__outgoing_edges:
            return False
        if (x, y) in self.__costs:
            return False

        self.__outgoing_edges[x].append(y)
        self.__incoming_edges[y].append(x)
        self.__costs[(x, y)] = cost
        self.__number_of_edges += 1
        return True

    def remove_edge(self, x, y):
        if (x not in self.__outgoing_edges) or (y not in self.__outgoing_edges):
            return False
        if (x, y) not in self.__costs:
            return False

        self.__outgoing_edges[x].remove(y)
        self.__incoming_edges[y].remove(x)
        del self.__costs[(x, y)]
        self.__number_of_edges -= 1
        return True

    def in_degree(self, v):
        if v not in self.__incoming_edges:
            return -1
        return len(self.__incoming_edges[v])

    def out_degree(self, v):
        if v not in self.__outgoing_edges:
            return -1
        return len(self.__outgoing_edges[v])

    def find_if_edge(self, x, y):
        if (x, y) in self.__costs:
            return self.__costs[(x, y)]
        return False

    def change_cost(self, x, y, c):
        if (x, y) not in self.__costs:
            return False
        self.__costs[(x, y)] = c
        return True

    def copy_graph(self):
        return copy.deepcopy(self)

    # ============================================================
    # ===================== LAB 2 – PB 4 =========================
    # ============================================================

    def __bfs_component(self, start, visited):
        queue = [start]
        comp = [start]
        visited.add(start)

        while queue:
            node = queue.pop(0)
            neighbors = set(self.__incoming_edges[node] + self.__outgoing_edges[node])
            for nb in neighbors:
                if nb not in visited:
                    visited.add(nb)
                    comp.append(nb)
                    queue.append(nb)
        return comp

    def connected_components_bfs(self):
        visited = set()
        comps = []
        for v in self.parse_vertices():
            if v not in visited:
                comps.append(self.__bfs_component(v, visited))
        return comps

    # ============================================================
    # ===================== LAB 2 – PB 3 =========================
    # ============================================================

    def __dfs_cc(self, v, visited, comp):
        visited.add(v)
        comp.append(v)
        neighbors = set(self.__incoming_edges[v] + self.__outgoing_edges[v])
        for nb in neighbors:
            if nb not in visited:
                self.__dfs_cc(nb, visited, comp)

    def connected_components_dfs(self):
        visited = set()
        comps = []
        for v in self.parse_vertices():
            if v not in visited:
                comp = []
                self.__dfs_cc(v, visited, comp)
                comps.append(comp)
        return comps

    # ============================================================
    # ================= LAB 2 BONUS – SCC (Kosaraju) ==============
    # ============================================================

    def __dfs_postorder_fill(self, v, visited, stack):
        visited.add(v)
        for nb in self.__outgoing_edges[v]:
            if nb not in visited:
                self.__dfs_postorder_fill(nb, visited, stack)
        stack.append(v)

    def __dfs_collect_scc(self, v, visited, comp, rev_edges):
        visited.add(v)
        comp.append(v)
        for nb in rev_edges[v]:
            if nb not in visited:
                self.__dfs_collect_scc(nb, visited, comp, rev_edges)

    def strongly_connected_components(self):
        visited = set()
        stack = []

        for v in self.parse_vertices():
            if v not in visited:
                self.__dfs_postorder_fill(v, visited, stack)

        reverse = {v: [] for v in self.parse_vertices()}
        for u in self.__outgoing_edges:
            for v in self.__outgoing_edges[u]:
                reverse[v].append(u)

        visited.clear()
        sccs = []

        while stack:
            node = stack.pop()
            if node not in visited:
                comp = []
                self.__dfs_collect_scc(node, visited, comp, reverse)
                sccs.append(comp)

        return sccs

    # ============================================================
    # ===================== LAB 4 – Pb2 ============
    # ============================================================

    def topo_sort_predecessor(self):
        indeg = {v: self.in_degree(v) for v in self.parse_vertices()}
        q = [v for v in indeg if indeg[v] == 0]
        topo = []

        while q:
            u = q.pop(0)
            topo.append(u)
            for nb in self.parse_outbound_edges(u):
                indeg[nb] -= 1
                if indeg[nb] == 0:
                    q.append(nb)

        if len(topo) != self.number_of_vertices:
            return False, []
        return True, topo

    def earliest_start_times(self, topo, durations):
        E = {v: 0 for v in topo}
        for u in topo:
            for v in self.parse_outbound_edges(u):
                E[v] = max(E[v], E[u] + durations[u])
        return E

    def total_project_time(self, E, durations):
        mx = 0
        for v in self.parse_vertices():
            if len(list(self.parse_outbound_edges(v))) == 0:
                mx = max(mx, E[v] + durations[v])
        return mx

    def latest_start_times(self, topo, durations, total):
        L = {v: float('inf') for v in topo}
        for v in topo:
            if len(list(self.parse_outbound_edges(v))) == 0:
                L[v] = total - durations[v]

        for u in reversed(topo):
            for v in self.parse_outbound_edges(u):
                L[u] = min(L[u], L[v] - durations[u])

        return L

    def critical_activities(self, E, L):
        return [v for v in E if E[v] == L[v]]


# ============================================================
# ===================== LAB 5 – PB 8 =========================
# ============================================================

def minimum_spanning_tree_prim(graph):
    n = graph.number_of_vertices
    visited = [0] * n
    mst = [[] for _ in range(n)]
    dist = [float("inf")] * n
    parent = [-1] * n

    dist[0] = 0
    for _ in range(n):
        u = -1
        best = float("inf")
        for i in range(n):
            if not visited[i] and dist[i] < best:
                best = dist[i]
                u = i

        if u == -1:
            break

        visited[u] = 1
        if parent[u] != -1:
            mst[u].append(parent[u])
            mst[parent[u]].append(u)

        for v in graph.parse_outbound_edges(u):
            cost = graph.costs.get((u, v), graph.costs.get((v, u)))
            if not visited[v] and cost < dist[v]:
                dist[v] = cost
                parent[v] = u

    return mst


def dfs_preorder(tree, node, visited, path):
    visited[node] = 1
    path.append(node)
    for nb in tree[node]:
        if not visited[nb]:
            dfs_preorder(tree, nb, visited, path)


def approximate_hamiltonian_cycle(graph):
    mst = minimum_spanning_tree_prim(graph)
    n = graph.number_of_vertices
    visited = [0] * n
    path = []

    dfs_preorder(mst, 0, visited, path)
    path.append(path[0])

    cost = 0
    for i in range(len(path) - 1):
        u, v = path[i], path[i+1]
        cost += graph.costs.get((u, v), graph.costs.get((v, u)))

    return path, cost
