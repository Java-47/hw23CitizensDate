package telran.citizens.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import telran.citizens.interfaces.Citizens;
import telran.citizens.model.Person;

public class CitizensImpl implements Citizens {
	private List<Person> idList;
	private List<Person> lastNameList;
	private List<Person> ageList;
	private static Comparator<Person> lastNameComparator;
	private static Comparator<Person> ageComparator;
	static {
		lastNameComparator = (p1, p2) -> {
			int res = p1.getLastName().compareTo(p2.getLastName());
			return res != 0 ? res : p1.compareTo(p2);
		};
		ageComparator = (p1, p2) -> {
			int res = Integer.compare(p1.getAge(), p2.getAge());
			return res != 0 ? res : p1.compareTo(p2);
		};
	}

	public CitizensImpl() {
		idList = new ArrayList<>();
		ageList = new ArrayList<>();
		lastNameList = new ArrayList<>();
	}

	public CitizensImpl(List<Person> citizens) {
		this();
//		for (Person person : citizens) {
//			if (person != null && !idList.contains(person)) {
//				idList.add(person);
//				ageList.add(person);
//				lastNameList.add(person);
//			}
//		}
//		Collections.sort(idList);
//		Collections.sort(ageList, ageComparator);
//		Collections.sort(lastNameList, lastNameComparator);
		for (Person person : citizens) {
			add(person);
		}
	}

	@Override
	// O(n)
	public boolean add(Person person) {
		if (person == null) {
			return false;
		}
		int index = Collections.binarySearch(idList, person);
		if (index >= 0) {
			return false;
		}
		index = -index - 1;
		idList.add(index, person);
		index = -Collections.binarySearch(ageList, person, ageComparator) - 1;
		ageList.add(index, person);
		index = -Collections.binarySearch(lastNameList, person, lastNameComparator) - 1;
		lastNameList.add(index, person);
		return true;
	}

	@Override
	// O(n)
	public boolean remove(int id) {
		Person victim = find(id);
		if (victim == null) {
			return false;
		}
		idList.remove(victim);
		ageList.remove(victim);
		lastNameList.remove(victim);
		return true;
	}

	@Override
	// O(log(n))
	public Person find(int id) {
		Person pattern = new Person(id, "", "", LocalDate.of(1997, 2, 5));
		int index = Collections.binarySearch(idList, pattern);
		return index < 0 ? null : idList.get(index);
	}

	@Override
	// O(log(n))
	public Iterable<Person> find(int minAge, int maxAge) {
	    LocalDate minAgeD = LocalDate.now().minusYears(minAge);
	    LocalDate maxAgeD = LocalDate.now().minusYears(maxAge);
		Person pattern = new Person(Integer.MIN_VALUE, null, null, minAgeD);
		int from = -Collections.binarySearch(ageList, pattern, ageComparator) - 1;
		pattern = new Person(Integer.MAX_VALUE, null, null, maxAgeD);
		int to = -Collections.binarySearch(ageList, pattern, ageComparator) - 1;
		return ageList.subList(from, to);
	}

	@Override
	// O(log(n))
	public Iterable<Person> find(String lastName) {
		Person pattern = new Person(Integer.MIN_VALUE, null, lastName, LocalDate.of(1997, 2, 5));
		int from = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;
		pattern = new Person(Integer.MAX_VALUE, null, lastName, LocalDate.of(1997, 2, 5));
		int to = -Collections.binarySearch(lastNameList, pattern, lastNameComparator) - 1;
		return lastNameList.subList(from, to);
	}

	@Override
	// O(1)
	public Iterable<Person> getAllPersonSortedById() {
		return idList;
	}

	@Override
	// O(1)
	public Iterable<Person> getAllPersonSortedByLastName() {
		return lastNameList;
	}

	@Override
	// O(1)
	public Iterable<Person> getAllPersonSortedByAge() {
		return ageList;
	}

	@Override
	// O(1)
	public int size() {
		return idList.size();
	}

}
