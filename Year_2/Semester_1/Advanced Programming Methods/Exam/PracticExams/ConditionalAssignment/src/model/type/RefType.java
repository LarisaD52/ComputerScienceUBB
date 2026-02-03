package model.type;

import model.value.IValue;
import model.value.RefValue;

public class RefType implements IType {
    private final IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object another) {
        if (another instanceof RefType)
            return inner.equals(((RefType) another).getInner());
        else
            return false;
    }

    @Override
    public IValue getDefaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }
}
