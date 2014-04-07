package mjc.asm;

import mjc.temp.Temp;
import mjc.temp.TempList;

public class MOVE extends Instr {
    public final Temp destination;
    public final Temp source;

    public MOVE(String asm, Temp destination, Temp source) {
        this.asm = asm;
        this.destination = destination;
        this.source = source;
    }

    @Override
    public TempList use() {
        return new TempList(source, null);
    }

    @Override
    public TempList def() {
        return new TempList(destination, null);
    }

    @Override
    public Targets jumps() {
        return null;
    }
}
