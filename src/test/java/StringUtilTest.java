import static org.junit.Assert.*;

import org.dbunit.dataset.builder.util.StringUtil;
import org.junit.Test;

public class StringUtilTest {

	@Test
	public void ensureCamelCaseNameNormalization() {
		assertEquals("model_with_composed_name", StringUtil.normalizeTableName("ModelWithComposedName"));
		assertEquals("a_model", StringUtil.normalizeTableName("AModel"));
		assertEquals("model", StringUtil.normalizeTableName("Model"));
		assertEquals("roots_blood_roots", StringUtil.normalizeTableName("RootsBloodRoots"));
	}

}
