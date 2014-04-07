package mjc.frame;

import java.util.List;

import mjc.asm.Instr;

public class Proc {
    public final String begin;
    public final List<Instr> body;
    public final String end;

    public Proc(String begin, List<Instr> body, String end) {
        this.begin = begin;
        this.body = body;
        this.end = end;
    }
}
