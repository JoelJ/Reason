package com.attask.jenkins;

import org.jvnet.hudson.test.HudsonTestCase;

import java.util.List;

/**
 * User: Joel Johnson
 * Date: 9/11/12
 * Time: 4:44 PM
 */
public class CategoryTest extends HudsonTestCase {
	public void testDepth() {
		String toParse =
						"cat1\n" +
						"cat2\n" +
						"	cat3\n" +
						"	cat4\n" +
						"		cat5\n" +
						"	cat6\n" +
						"		cat7\n" +
						"cat8";
		Category rootCategory = Category.parse(toParse);
		assertNotNull("rootLevel", rootCategory);

		List<Category> rootLevel = rootCategory.getSubCategories();
		assertEquals("rootLevel", 3, rootLevel.size());
		assertEquals("cat1", rootLevel.get(0).getName());
		assertEquals("cat2", rootLevel.get(1).getName());
		assertEquals("cat8", rootLevel.get(2).getName());

		List<Category> tabbedOnce = rootLevel.get(1).getSubCategories();
		assertEquals("tabbedOnce", 3, rootLevel.size());
		assertEquals("cat3", tabbedOnce.get(0).getName());
		assertEquals("cat4", tabbedOnce.get(1).getName());
		assertEquals("cat6", tabbedOnce.get(2).getName());

		List<Category> tabbedTwice_1 = tabbedOnce.get(1).getSubCategories();
		assertEquals("tabbedTwice_1", 1, tabbedTwice_1.size());
		assertEquals("cat5", tabbedTwice_1.get(0).getName());


		List<Category> tabbedTwice_2 = tabbedOnce.get(2).getSubCategories();
		assertEquals("tabbedTwice_2", 1, tabbedTwice_2.size());
		assertEquals("cat7", tabbedTwice_2.get(0).getName());
	}

	public void testBadDepth() {
		String toParse =
				"cat1\n" +
				"\t\tcat2";
		Category rootCategory = Category.parse(toParse);
		assertEquals("Should nest cat2 inside cat1", 1, rootCategory.getSubCategories().size());

		Category cat1 = rootCategory.getSubCategories().get(0);
		assertEquals("cat1", cat1.getName());
		assertEquals("Should nest cat2 inside cat1", 1, cat1.getSubCategories().size());

		Category cat2 = cat1.getSubCategories().get(0);
		assertEquals("cat2", cat2.getName());
	}
}
