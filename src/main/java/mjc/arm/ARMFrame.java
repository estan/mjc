package mjc.arm;

import java.util.List;

import mjc.asm.Instr;
import mjc.frame.Access;
import mjc.frame.Frame;
import mjc.frame.Proc;
import mjc.temp.Label;
import mjc.temp.Temp;
import mjc.temp.TempList;
import mjc.temp.TempMap;
import mjc.tree.Exp;
import mjc.tree.ExpList;
import mjc.tree.Stm;

public class ARMFrame implements Frame {

    @Override
    public Label name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Access> formals() {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Temp FP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int wordSize() {
        // TODO Auto-generated method stub
        return 0;
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
