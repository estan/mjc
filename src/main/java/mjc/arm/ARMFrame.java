package mjc.arm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mjc.asm.Instr;
import mjc.frame.Access;
import mjc.frame.Frame;
import mjc.frame.Proc;
import mjc.temp.Label;
import mjc.temp.Temp;
import mjc.temp.TempList;
import mjc.temp.TempMap;
import mjc.tree.CALL;
import mjc.tree.CONST;
import mjc.tree.Exp;
import mjc.tree.ExpList;
import mjc.tree.NAME;
import mjc.tree.Stm;

public class ARMFrame implements Frame {

    private Temp reg_lr;

    private List<Access> formals;
    private Label name;
    private Map<String, Label> labels;

    ARMFrame(Label name, List<Boolean> escapes) {
        this.name = name;
        this.labels = new HashMap<>();

        this.reg_lr = new Temp();

        formals = new ArrayList<>();
        for (@SuppressWarnings("unused") Boolean escape : escapes) {
            formals.add(new ARMInReg(new Temp()));
        }
    }

    @Override
    public Label name() {
        return name;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Access> formals() {
        return formals;
    }

    @Override
    public Access allocLocal(boolean escape) {
        // More to do?
        return new ARMInReg(new Temp());
    }

    @Override
    public Access accessOutgoing(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Exp externalCall(String func, ExpList args) {
        Label label = labels.get(func);
        if (label == null) {
            label = new Label(func);
            labels.put(func, label);
        }
        return new CALL(new NAME(label), new ExpList(new CONST(0), args));
    }

    @Override
    public Stm procEntryExit1(Stm body) {
        // TODO Auto-generated method stub
        return body;
    }

    @Override
    public List<Instr> procEntryExit2(List<Instr> inst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Proc procEntryExit3(List<Instr> body) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Temp RV() {
        return reg_lr;
    }

    @Override
    public Temp FP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int wordSize() {
        return 4;
    }

    @Override
    public List<Instr> codegen(Stm stm) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempMap initial() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TempList registers() {
        // TODO Auto-generated method stub
        return null;
    }
}
