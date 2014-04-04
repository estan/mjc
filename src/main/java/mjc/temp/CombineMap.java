package mjc.temp;

public class CombineMap implements TempMap {
    final TempMap firstMap;
    final TempMap secondMap;

    public CombineMap(final TempMap firstMap, final TempMap secondMap) {
        this.firstMap = firstMap;
        this.secondMap = secondMap;
    }

    public String tempMap(final Temp temp) {
        final String s = firstMap.tempMap(temp);
        if (s != null) {
            return s;
        }
        return secondMap.tempMap(temp);
    }
}
