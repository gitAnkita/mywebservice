package hello;

import com.aerospike.client.*;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.ManagedBean;
import java.util.Map;
import java.util.concurrent.Callable;

@ManagedBean
public class BaseCache implements Cache {

	private AerospikeClient client;

	private WritePolicy writePolicy;

	public BaseCache(){
		client = new AerospikeClient("localhost", 3000);
		writePolicy = new WritePolicy();
		writePolicy.expiration = 120;
	}

	protected Key getKey(Object key) {
		return new Key("switch", "mywebservice", key.toString());
	}

	private ValueWrapper toWrapper(Record record) {
		return (record != null ? new SimpleValueWrapper(record.getValue("payables")) : null);
	}

	@Override
	public void clear() {
    ScanPolicy scanPolicy = new ScanPolicy();
    scanPolicy.includeBinData = false;

	  client.scanAll(scanPolicy, "switch", "mywebservice", new ScanCallback() {
      @Override
      public void scanCallback(Key key, Record record) throws AerospikeException {
        client.delete(writePolicy,key);
      }
    });
	}

	@Override
	public void evict(Object key) {
		client.delete(writePolicy, getKey(key));
	}

	@Override
	public ValueWrapper get(Object key) {
		Record record = client.get(null, getKey(key));
		if (record == null) {
			return null;
		} else {
			ValueWrapper vr = toWrapper(record);
			return vr;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {

		Record record = client.get(writePolicy, getKey(key));
		if (record == null) {
			return null;
		} else {
			return (T) record.getValue("payables");
		}
	}

	public Record getRawRecord(Object key){
	  return client.get(writePolicy,getKey(key));
  }

	@Override
	public String getName() {

		return "switch" + ":" + "mywebservice";
	}

	@Override
	public Object getNativeCache() {

		return client;
	}

	@Override
	public void put(Object key, Object value) {
		client.put(writePolicy, getKey(key), new Bin("payables", value));
	}

  public void putBinAsMap(Object key, Map<String,Object> value) {
    client.put(writePolicy, getKey(key), new Bin("payables", Value.getAsMap(value)));
  }

  public void putMultipleBins(Object key,Bin... value){
    client.put(writePolicy,getKey(key),value);
  }

  @Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		Record record = client.operate(writePolicy, getKey(key),
			Operation.put(new Bin("payables", value)), Operation.get("payables")
		);
		return toWrapper(record);
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		client.close();
		super.finalize();
	}
}