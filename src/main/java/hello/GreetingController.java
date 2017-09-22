package hello;

import com.aerospike.client.Bin;
import com.aerospike.client.Record;
import io.spring.guides.gs_producing_web_service.Country;
import kamon.Kamon;
import kamon.util.JavaTags;
import org.luaj.vm2.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@RestController
public class GreetingController {

  private static final String template = "Hello, %s!";
  @Value("${spring.project.name}")
  private static String project;
  private final AtomicLong counter = new AtomicLong();

  @Autowired
  private GreetingRepository greetingRepository;

  @Autowired
  private Client client;

  @Autowired
  private ProfileRepository profileRepository;

  @Autowired
  private GreetingHibernateRepository greetingHibernateRepository;

  @Autowired
  private BaseCache cacheInstance;

  @RequestMapping(value = "/getGreeting", method = RequestMethod.GET)
  public Iterable<Greeting> getGreeting(@RequestParam(value = "sender", defaultValue = "") String sender) {
    System.out.println("in getGreeting");
    System.out.println("project.name-" + project);
    ArrayList<Greeting> greetingArrayList = (ArrayList<Greeting>) greetingRepository
      .findBySender_Name(sender);
    Map<String, String> tags = new HashMap<>();
    tags.put("getGreeting", "200");
    Kamon.metrics().counter("mywebservice", JavaTags.tagsFromMap(tags)).increment();

    return greetingArrayList;
  }

  @RequestMapping(value = "/addGreeting", method = RequestMethod.POST)
  public void addGreeting(@RequestParam(value = "content", defaultValue = "hello") String content) {
    greetingRepository.save(new Greeting(content));
  }

  @RequestMapping(value = "/soapclient", method = RequestMethod.GET)
  public Country soapclient(@RequestParam(value = "name", defaultValue = "") String name) {
    Country country = client.callService(name);
    return country;
  }

  @RequestMapping(value = "/getSenders", method = RequestMethod.GET)
  public Iterable<Profile> getSenders() {
    Iterable<Profile> senders = profileRepository.getAllPersons();
    senders.forEach(sender->{
      Bin bin1 = new Bin("name", sender.getName());
      Bin bin2 = new Bin("age",sender.getAge());
      cacheInstance.putMultipleBins(""+sender.getId(),bin1,bin2);
    });
    return senders;
  }

  @RequestMapping(value = "/getSenderById", method = RequestMethod.GET)
  public Profile getSenderById(@RequestParam(value = "id", defaultValue = "") String id) {
    Record record = cacheInstance.getRawRecord(id);
    if(record == null){
      return null;
    }
    System.out.println(record.toString());
    Profile profile = new Profile();
    profile.setId(Long.valueOf(id));
    profile.setAge((Long) record.getValue("age"));
    profile.setName((String)record.getValue("name"));
    //cacheInstance.evict(id);
    //cacheInstance.clear();
    return profile;
  }

  @RequestMapping(value = "/getSenderByFilter", method = RequestMethod.GET)
  public Iterable<Profile> getSenderByName(@RequestParam(value = "name") String name,
                                           @RequestParam(value = "minAge") int minAge){
    return profileRepository.getPersonByNameAndAge(name,minAge);
  }

  @RequestMapping(value = "/greetingBySenderHibernate",method = RequestMethod.GET)
  public Iterable<Greeting> getGreetingBySenderName(@RequestParam(value = "name") String name){
    return greetingHibernateRepository.getGreetingBySenderName(name);
  }
}