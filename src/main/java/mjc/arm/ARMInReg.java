package mjc.arm;

import mjc.frame.Access;
import mjc.temp.Temp;
import mjc.tree.Exp;
import mjc.tree.TEMP;

public class ARMInReg implements Access {
    final Temp name;

    ARMInReg(Temp name) {
        this.name = name;
    }

    @Override
    public Exp exp(Exp basePointer) {
        return new TEMP(name);
    }
}
