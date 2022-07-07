package br.com.acenetwork.commons.manager;

import java.util.ResourceBundle;

public interface BundleSupplier <T>
{
	T get(ResourceBundle bundle, Object... args);
}