package contextlabs.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.google.common.collect.ImmutableMap;

import contextlabs.model.Product;
import contextlabs.model.attributes.Attribute;
import contextlabs.model.attributes.ProductAttributes;
import reactor.core.publisher.Flux;

@Service
public final class CsvProductLoader implements ProductLoader {
	private static final CSVFormat CSV_FORMAT = CSVFormat.RFC4180.builder()
			.setHeader()
			.setSkipHeaderRecord(true)
			.build();

	@Value(value = "${products-csv}")
	private String fileName;

	private Reader fileReader = null;
	private CSVParser csvParser = null;
	private ImmutableMap<String, ProductAttributes> headerMapping = null;
	
	@Override
	public Flux<Product> getProducts() {
		if (this.csvParser == null) {
			this.csvParser = getCsvParser();
		}
		return Flux
				.fromIterable(this.csvParser)
				.flatMap(csvRecord ->
					Flux.just(new Product(csvRecord.getRecordNumber(), productAttributes(csvRecord)))
				)
				.filter(product -> product != null)
				.doOnComplete(() -> {
					try {
						this.csvParser.close();
						this.fileReader.close();
					} catch (IOException e) {
						// NOP
					}
					this.csvParser = null;
					this.headerMapping = null;
				});
	}
	
	private ImmutableMap<ProductAttributes, Attribute<? extends Serializable>> productAttributes(CSVRecord csvRecord) {
		if (this.headerMapping == null) {
			Map<String, ProductAttributes> hm = new HashMap<>(csvRecord.getParser().getHeaderNames().size());
			for (var headerName: csvRecord.getParser().getHeaderNames()) {
				ProductAttributes headerAttribute = ProductAttributes.fromString(headerName.toUpperCase());
				if (headerAttribute != null) {
					hm.put(headerName, headerAttribute);
				}
			}
			this.headerMapping = ImmutableMap.copyOf(hm);
		}
		
		Map<ProductAttributes, Attribute<? extends Serializable>> productAttributes = new HashMap<>(headerMapping.size());
		for (String headerName: this.headerMapping.keySet()) {
			ProductAttributes attribute = this.headerMapping.get(headerName);
			Attribute<? extends Serializable> value = attribute.getValue(csvRecord.get(headerName));
			if (value != null) {
				productAttributes.put(attribute, value);
			}
		}
		if (productAttributes.isEmpty()) {
			return null;
		}
		return ImmutableMap.copyOf(productAttributes);
	}
	
	private CSVParser getCsvParser() {
		try {
			fileReader = new InputStreamReader(ResourceUtils.getURL(fileName).openStream());
			return CSV_FORMAT.parse(fileReader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
