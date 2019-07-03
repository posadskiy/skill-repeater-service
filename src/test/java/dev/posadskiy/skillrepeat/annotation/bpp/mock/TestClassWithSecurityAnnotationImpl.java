package dev.posadskiy.skillrepeat.annotation.bpp.mock;

import dev.posadskiy.skillrepeat.annotation.Security;
import dev.posadskiy.skillrepeat.rest.RequestWrapper;

public class TestClassWithSecurityAnnotationImpl implements TestClassWithSecurityAnnotation {

	@Security
	public void testMethod(RequestWrapper requestWrapper) {}
}
