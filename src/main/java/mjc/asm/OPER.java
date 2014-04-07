package mjc.asm;

import mjc.temp.LabelList;
import mjc.temp.TempList;

public class OPER extends Instr {
    public TempList destinations;
    public TempList sources;
    public Targets targets;

    public OPER(String asm, TempList destinations, TempList sources, LabelList targetLabels) {
        this.asm = asm;
        this.destinations = destinations;
        this.sources = sources;
        this.targets = new Targets(targetLabels);
    }

    public OPER(String asm, TempList destinations, TempList sources) {
        this.asm = asm;
        this.destinations = destinations;
        this.sources = sources;
        this.targets = null;
    }

    @Override
    public TempList use() {
        return sources;
    }

    @Override
    public TempList def() {
        return destinations;
    }

    @Override
    public Targets jumps() {
        return targets;
    }

}
