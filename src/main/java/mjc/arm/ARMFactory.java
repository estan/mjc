package mjc.arm;

import java.util.List;

import mjc.frame.Factory;
import mjc.frame.Frame;
import mjc.frame.Record;
import mjc.temp.Label;

public class ARMFactory implements Factory {

    @Override
    public Frame newFrame(Label name, List<Boolean> escapes) {
        return new ARMFrame(name, escapes);
    }

    @Override
    public Record newRecord(String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
