package com.attask.jenkins;

import java.io.Serializable;
import java.util.*;

/**
 * User: Joel Johnson
 * Date: 9/11/12
 * Time: 1:25 PM
 */
public class Category implements Serializable {
	private final String id;
	private final String name;
	private final int depth;
	private final List<Category> subCategories;

	public static Category parse(String categoryString) {
		Scanner scanner = new Scanner(categoryString);
		Category root = new Category("root", "root", -1);
		Map<Integer, Category> lastOfDepth = new HashMap<Integer, Category>();
		lastOfDepth.put(-1, root);
		int previousDepth = -1;
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			int depth = depth(line);
			if(depth - previousDepth > 1) {
				depth = previousDepth + 1;
			}
			Category parent = lastOfDepth.get(depth - 1);

			String name = line.trim();
			String id = parent.getId() + "-" + name;
			Category category = new Category(id, name, depth);
			lastOfDepth.put(depth, category);

			parent.addSubCategory(category);
			previousDepth = depth;
		}

		return root;
	}

	private static int depth(String line) {
		if(line == null || line.isEmpty()) {
			return -1;
		}
		for(int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if(!Character.isWhitespace(c)) {
				return i;
			}
		}
		return -1;
	}

	private Category(String id, String name, int depth) {
		this.id = id;
		this.name = name;
		this.depth = depth;
		this.subCategories = new ArrayList<Category>();
	}

	private void addSubCategory(Category category) {
		subCategories.add(category);
	}

	public List<Category> getSubCategories() {
		return Collections.unmodifiableList(new ArrayList<Category>(subCategories));
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getDepth() {
		return depth;
	}

	@Override
	public String toString() {
		return "Category{"+name+"}";
	}
}
