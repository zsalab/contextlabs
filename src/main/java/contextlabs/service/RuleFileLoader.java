package contextlabs.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.BaseStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import contextlabs.components.RuleParser;
import contextlabs.model.query.Rule;
import reactor.core.publisher.Flux;

@Service
public final class RuleFileLoader implements RuleLoader {
	@Autowired
	private RuleParser ruleParser;

	@Value(value = "${rules-file}")
	private String fileName;

	public List<Rule> getRules() {
		return Flux.using(() -> new BufferedReader(new InputStreamReader(ResourceUtils.getURL(fileName).openStream())).lines(),
				Flux::fromStream,
				BaseStream::close)
		.flatMap(line -> Flux.just(ruleParser.parseRule(line)))
		.filter(product -> product != null)
		.collectList()
		.block();
	}
}
