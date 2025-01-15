//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.atcraftmc.qlib.task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MultiMap<K, V> implements Map<K, V> {
    private final HashMap<K, V> kvHashMap = new HashMap<>();
    private final HashMap<V, K> vkHashMap = new HashMap<>();

    public MultiMap() {
    }

    public V put(K k, V v) {
        this.kvHashMap.put(k, v);
        this.vkHashMap.put(v, k);
        return v;
    }

    public int size() {
        return this.kvHashMap.size();
    }

    public boolean isEmpty() {
        return this.kvHashMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.kvHashMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.kvHashMap.containsValue(value);
    }

    public V get(Object obj) {
        return this.kvHashMap.get(obj);
    }

    public K of(V v) {
        return this.vkHashMap.get(v);
    }

    public V remove(Object k) {
        this.vkHashMap.remove(this.kvHashMap.get(k));
        return this.kvHashMap.remove(k);
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        this.kvHashMap.putAll(m);
    }

    public void clear() {
        this.kvHashMap.clear();
        this.vkHashMap.clear();
    }

    public Set<K> keySet() {
        return this.kvHashMap.keySet();
    }

    public Collection<V> values() {
        return this.kvHashMap.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return this.kvHashMap.entrySet();
    }
}
