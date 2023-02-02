package contextlabs.model;

import java.io.Serializable;

import com.google.common.collect.ImmutableMap;

import contextlabs.model.attributes.Attribute;
import contextlabs.model.attributes.ProductAttributes;
import lombok.Value;

@Value
public class Product {
	protected final long id;
	
	private final ImmutableMap<ProductAttributes, Attribute<? extends Serializable>> attributes;
}
