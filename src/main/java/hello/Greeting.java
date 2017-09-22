package hello;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "greeting")
@NoArgsConstructor
@NamedEntityGraph(name = "Greeting.senders",
  attributeNodes = @NamedAttributeNode("sender"))
public class Greeting {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String content;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender", referencedColumnName = "id")
  private Profile sender;

  public Greeting(String content){
    this.content = content;
  }

}