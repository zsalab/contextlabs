package contextlabs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import contextlabs.service.ScoringService;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ScoringService scoringService) {
    return args -> {
    	var scoreResult = scoringService.scoreProducts();
    	System.out.println("Products scores (with positive score): ");
    	scoreResult.getProductScores().forEach((product, score) -> {
        	System.out.println("    "+product+" "+score);
    	});
    	System.out.println("Total price: "+scoreResult.getTotalPrice());
    	System.out.println("Average price: "+scoreResult.getTotalPrice());
    };
  }
}
