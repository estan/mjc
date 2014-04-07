package mjc.asm;

import mjc.temp.Label;
import mjc.temp.LabelList;
import mjc.temp.Temp;
import mjc.temp.TempList;
import mjc.temp.TempMap;

public abstract class Instr {
    public String asm;

    public abstract TempList use();

    public abstract TempList def();

    public abstract Targets jumps();

    private Temp nthTemp(TempList temps, int i) {
        if (i == 0) {
            return temps.head;
        } else {
            return nthTemp(temps.tail, i - 1);
        }
    }

    private Label nthLabel(LabelList labels, int i) {
        if (i == 0) {
            return labels.head;
        } else {
            return nthLabel(labels.tail, i - 1);
        }
    }

    public String format(TempMap m) {
        final TempList destination = def();
        final TempList source = use();
        final Targets targets = jumps();
        final LabelList targetLabels = (targets == null) ? null : targets.targetLabels;
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < asm.length(); i++) {
            if (asm.charAt(i) == '`') {
                switch (asm.charAt(++i)) {
                    case 's': {
                        int n = Character.digit(asm.charAt(++i), 10);
                        builder.append(m.tempMap(nthTemp(source, n)));
                        break;
                    }
                    case 'd': {
                        int n = Character.digit(asm.charAt(++i), 10);
                        builder.append(m.tempMap(nthTemp(destination, n)));
                        break;
                    }
                    case 'j': {
                        int n = Character.digit(asm.charAt(++i), 10);
                        builder.append(nthLabel(targetLabels, n).toString());
                        break;
                    }
                    case '`':
                        builder.append('`');
                        break;
                    default:
                        throw new Error("bad asm format");
                }
            } else {
                builder.append(asm.charAt(i));
            }
        }

        return builder.toString();
    }
}
