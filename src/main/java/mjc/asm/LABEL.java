package mjc.asm;

import mjc.temp.Label;
import mjc.temp.TempList;

public class LABEL extends Instr {
    public final Label label;

    public LABEL(String asm, Label label) {
        this.asm = asm;
        this.label = label;
    }

    @Override
    public TempList use() {
        return null;
    }

    @Override
    public TempList def() {
        return null;
    }

    @Override
    public Targets jumps() {
        return null;
    }
}
