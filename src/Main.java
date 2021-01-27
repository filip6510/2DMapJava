
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import java.util.Set;


/**
 * Interface providing 2-dimensional map.
 * It can be viewed as rows and cells sheet, with row keys and column keys.
 * Row and Column keys are using standard comparision mechanisms.
 *
 * @param <R> type of row keys
 * @param <C> type column keys
 * @param <V> type of values
 */
interface Map2D<R, C, V> {

    /**
     * Puts a value to the map, at given row and columns keys.
     * If specified row-column key already contains element, it should be replaced.
     *
     * @param rowKey row part of the key.
     * @param columnKey column part of the key.
     * @param value object to put. It can be null.
     * @return object, that was previously contained by map within these coordinates, or {@code null} if it was empty.
     * @throws NullPointerException if rowKey or columnKey are {@code null}.
     */
    V put(R rowKey, C columnKey, V value);

    /**
     * Gets a value from the map from given key.
     *
     * @param rowKey row part of a key.
     * @param columnKey column part of a key.
     * @return object contained at specified key, or {@code null}, if the key does not contain an object.
     */
    V get(R rowKey, C columnKey);

    /**
     * Gets a value from the map from given key. If specified value does not exist, returns {@code defaultValue}.
     *
     * @param rowKey row part of a key.
     * @param columnKey column part of a key.
     * @param defaultValue value to be be returned, if specified key does not contain a value.
     * @return object contained at specified key, or {@code defaultValue}, if the key does not contain an object.
     */
    V getOrDefault(R rowKey, C columnKey, V defaultValue);

    /**
     * Removes a value from the map from given key.
     *
     * @param rowKey row part of a key.
     * @param columnKey column part of a key.
     * @return object contained at specified key, or {@code null}, if the key didn't contain an object.
     */
    V remove(R rowKey, C columnKey);

    /**
     * Checks if map contains no elements.
     * @return {@code true} if map doesn't contain any values; {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * Checks if map contains any element.
     * @return {@code true} if map contains at least one value; {@code false} otherwise.
     */
    boolean nonEmpty();

    /**
     * Return number of values being stored by this map.
     * @return number of values being stored
     */
    int size();

    /**
     * Removes all objects from a map.
     */
    void clear();

    /**
     * Returns a view of mappings for specified key.
     * Result map should be immutable. Later changes to this map should not affect result map.
     *
     * @param rowKey row key to get view map for.
     * @return map with view of particular row. If there is no values associated with specified row, empty map is returned.
     */
    Map<C, V> rowView(R rowKey);

    /**
     * Returns a view of mappings for specified column.
     * Result map should be immutable. Later changes to this map should not affect returned map.
     *
     * @param columnKey column key to get view map for.
     * @return map with view of particular column. If there is no values associated with specified column, empty map is returned.
     */
    Map<R, V> columnView(C columnKey);

    /**
     * Checks if map contains specified value.
     * @param value value to be checked
     * @return {@code true} if map contains specified value; {@code false} otherwise.
     */
    boolean hasValue(V value);

    /**
     * Checks if map contains a value under specified key.
     * @param rowKey row key to be checked
     * @param columnKey column key to be checked
     * @return {@code true} if map contains specified key; {@code false} otherwise.
     */
    boolean hasKey(R rowKey, C columnKey);

    /**
     * Checks if map contains at least one value within specified row.
     * @param rowKey row to be checked
     * @return {@code true} if map at least one value within specified row; {@code false} otherwise.
     */
    boolean hasRow(R rowKey);

    /**
     * Checks if map contains at least one value within specified column.
     * @param columnKey column to be checked
     * @return {@code true} if map at least one value within specified column; {@code false} otherwise.
     */
    boolean hasColumn(C columnKey);

    /**
     * Return a view of this map as map of rows to map of columns to values.
     * Result map should be immutable. Later changes to this map should not affect returned map.
     *
     * @return map with row-based view of this map. If this map is empty, empty map should be returned.
     */
    Map<R, Map<C,V>> rowMapView();

    /**
     * Return a view of this map as map of columns to map of rows to values.
     * Result map should be immutable. Later changes to this map should not affect returned map.
     *
     * @return map with column-based view of this map. If this map is empty, empty map should be returned.
     */
    Map<C, Map<R,V>> columnMapView();

    /**
     * Fills target map with all key-value maps from specified row.
     *
     * @param target map to be filled
     * @param rowKey row key to get data to fill map from
     * @return this map (floating)
     */
    Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey);

    /**
     * Fills target map with all key-value maps from specified row.
     *
     * @param target map to be filled
     * @param columnKey column key to get data to fill map from
     * @return this map (floating)
     */
    Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey);

    /**
     * Puts all content of {@code source} map to this map.
     *
     * @param source map to make a copy from
     * @return this map (floating)
     */
    Map2D<R, C, V>  putAll(Map2D<? extends R, ? extends C, ? extends V> source);

    /**
     * Puts all content of {@code source} map to this map under specified row.
     * Ech key of {@code source} map becomes a column part of key.
     *
     * @param source map to make a copy from
     * @param rowKey object to use as row key
     * @return this map (floating)
     */
    Map2D<R, C, V>  putAllToRow(Map<? extends C, ? extends V> source, R rowKey);

    /**
     * Puts all content of {@code source} map to this map under specified column.
     * Ech key of {@code source} map becomes a row part of key.
     *
     * @param source map to make a copy from
     * @param columnKey object to use as column key
     * @return this map (floating)
     */
    Map2D<R, C, V>  putAllToColumn(Map<? extends R, ? extends V> source, C columnKey);

    /**
     * Creates a copy of this map, with application of conversions for rows, columns and values to specified types.
     * If as result of row or column convertion result key duplicates, then appriopriate row and / or column in target map has to be merged.
     * If merge ends up in key duplication, then it's up to specific implementation which value from possible to choose.
     *
     * @param rowFunction function converting row part of key
     * @param columnFunction function converting column part of key
     * @param valueFunction function converting value
     * @param <R2> result map row key type
     * @param <C2> result map column key type
     * @param <V2> result map value type
     * @return new instance of {@code Map2D} with converted objects
     */
    <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction);

    /**
     * Creates new instance of empty Map2D with default implementation.
     *
     * @param <R> row key type
     * @param <C> column key type
     * @param <V> value type
     * @return new instance of {@code Map2D}
     */
    static <R,C,V> Map2D<R,C,V> createInstance() {
        return new MyMap<R, C, V>();
    }
}
class MyMap<R, C, V> implements Map2D<R, C, V>
{

    Map<R,Map<C,V>> data;
    public MyMap ()
    {
        data = new HashMap<>();
    }
    public V put (R rowKey, C columnKey, V value)
    {
        if (rowKey == null || columnKey == null)
            throw new NullPointerException();
        V result = remove(rowKey, columnKey);
        if(!data.containsKey(rowKey))
            data.put(rowKey,new HashMap<>());
        data.get(rowKey).put(columnKey,value);
        return  result;
    }
    public V get(R rowKey, C columnKey)
    {
        if(data.get(rowKey) == null)
            return  null;
        return data.get(rowKey).get(columnKey);
    }
    public V getOrDefault(R rowKey, C columnKey, V defaultValue)
    {
        V result = get(rowKey,columnKey);
        if(result == null)
            return  defaultValue;
        return result;
    }
    public V remove(R rowKey, C columnKey)
    {
        if(!data.containsKey(rowKey))
            return null;
        return data.get(rowKey).remove(columnKey);
    }
    public boolean isEmpty()
    {
        return data.isEmpty();
    }
    public boolean nonEmpty()
    {
        return !data.isEmpty();
    }
    public int size()
    {
        int result = 0;
        for (var x : data.keySet())
            result += data.get(x).size();
        return result;
    }
    public void clear()
    {
        data.clear();
    }
    public Map<C, V> rowView(R rowKey)
    {
        if(!data.containsKey(rowKey))
            return new HashMap<>();
        return  new HashMap<>(data.get(rowKey));
    }
    public Map<R, V> columnView(C columnKey)
    {
        Map<R, V> result = new HashMap<>();
        for (var x : data.keySet())
            if(data.get(x).containsKey(columnKey))
                result.put(x, get(x,columnKey));
        return result;
    }
    public boolean hasValue(V value)
    {
        for (var x : data.keySet())
            if (data.get(x).containsValue(value))
                return true;
        return false;
    }
    public boolean hasKey(R rowKey, C columnKey)
    {
        if(!data.containsKey(rowKey))
            return false;
        return data.get(rowKey).containsKey(columnKey);
    }
    public boolean hasRow(R rowKey)
    {
        return data.containsKey(rowKey);
    }
    public boolean hasColumn(C columnKey)
    {
        for (var x : data.keySet())
            if(data.get(x).containsKey(columnKey))
                return true;
        return false;
    }
    public Map<R, Map<C,V>> rowMapView()
    {
        Map<R, Map<C, V>> result = new HashMap<>();
        for (var x : data.keySet())
            result.put(x,new HashMap<>(data.get(x)));
        return  result;
    }
    public Map<C, Map<R,V>> columnMapView()
    {
        Map<C, Map<R, V>> result = new HashMap<>();
        for (var x : data.keySet())
            for (var y : data.get(x).keySet())
                if(!result.containsKey(y))
                    result.put(y, columnView(y));
        return result;
    }
    public Map2D<R, C, V> fillMapFromRow(Map<? super C, ? super V> target, R rowKey)
    {
        Map<C,V> rowMap = rowView(rowKey);
        Set<C> aux = rowMap.keySet();
        for (var x : aux)
             target.put(x, rowMap.get(x));
        return this;
    }
    public Map2D<R, C, V> fillMapFromColumn(Map<? super R, ? super V> target, C columnKey)
    {
        Map<R,V> columnViewMap = columnView(columnKey);
        Set<R> aux = columnViewMap.keySet();
        for (var x : aux)
            target.put(x, columnViewMap.get(x));
        return this;
    }

    public  Map2D<R, C, V>  putAllToRow(Map<? extends C, ? extends V> source, R rowKey)
    {
        Set<?> aux = source.keySet();
        for(var x : aux)
            put(rowKey,(C)x,source.get(x));
        return this;
    }
    public Map2D<R, C, V>  putAllToColumn(Map<? extends R, ? extends V> source, C columnKey)
    {
        Set<?> aux = source.keySet();
        for (var x : aux)
            put((R)x,columnKey,source.get(x));
        return this;
    }
    public  Map2D<R, C, V>  putAll(Map2D<? extends R, ? extends C, ? extends V> source)
    {
        var auxMap = source.rowMapView();
        var rowSet = auxMap.keySet();
        for (var x : rowSet)
        {
            var columnSet = auxMap.get(x).keySet();
            for (var y : columnSet)
                put(x,y,auxMap.get(x).get(y));
        }
        return  null;
    }
    public  <R2, C2, V2> Map2D<R2, C2, V2> copyWithConversion(
            Function<? super R, ? extends R2> rowFunction,
            Function<? super C, ? extends C2> columnFunction,
            Function<? super V, ? extends V2> valueFunction)
    {
        MyMap<R2,C2,V2> result = new MyMap<>();

        for  (var x : data.keySet())
            for (var y : data.get(x).keySet())
                result.put((rowFunction.apply(x)),columnFunction.apply(y),valueFunction.apply(get(x,y)));
        return result;
    }

}
public class Main
{
    static Map2D<String, Integer, Double> create2x2map() {
        Map2D<String, Integer, Double> map = Map2D.createInstance();
        map.put("A", 1, 2.3);
        map.put("A", 2, 2.4);
        map.put("B", 1, 2.5);
        map.put("B", 2, 2.6);
        return map;
    }
    static void rowMapView() {
        Map2D<String, Integer, Double> map = create2x2map();

        Map<String, Map<Integer, Double>> result = map.rowMapView();
        System.out.println(result.size());//assertThat(result.size()).isEqualTo(2);
        Map<Integer, Double> resultA = result.get("A");
        System.out.println(resultA.size());//assertThat(resultA.size()).isEqualTo(2);
        System.out.println(resultA.get(1));//assertThat(resultA.get(colKey1)).isEqualTo(valA1);

        map.remove("A", 1);
        System.out.println(result.size());//assertThat(result.size()).isEqualTo(2);
        resultA = result.get("A");
        System.out.println(resultA.size());//assertThat(resultA.size()).isEqualTo(2);
        System.out.println(resultA.get(1));//assertThat(resultA.get(colKey1)).isEqualTo(valA1);
    }
    static  void fillMapFromRow() {
        Map2D<String, Integer, Double> map = create2x2map();

        HashMap<Integer, Double> toFill = new HashMap<>();
        map.fillMapFromRow(toFill, "A");

        //assertThat(toFill).size().isEqualTo(2);
     //   assertThat(toFill.get(colKey1)).isEqualTo(valA1);
        //assertThat(toFill.get(colKey2)).isEqualTo(valA2);

        map.fillMapFromRow(toFill, "C");
       // assertThat(toFill).size().isEqualTo(2);
    }
    public static void main(String[] args)
    {
      fillMapFromRow();
        MyMap<Integer,Integer,Integer> asd = new MyMap<>();
        asd.put(1,2,3);
        asd.put(1,2,3);
        asd.put(1,2,3);
        //System.out.print(asd.hasKey(1,2));
    }
}
