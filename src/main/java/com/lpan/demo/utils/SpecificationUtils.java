package com.lpan.demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationUtils {

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String IS = "IS";

	public static final String OR_SEPARATOR = "_OR_";

	public static Map<String, Operator> OPERATOR_MAP = new HashMap<>();
	public static Map<String, IsType> ISTYPE_MAP = new HashMap<>();

	static {
		Operator[] operators = Operator.values();
		for (Operator operator : operators) {
			OPERATOR_MAP.put(operator.name(), operator);
		}

		IsType[] isTypes = IsType.values();
		for (IsType isType : isTypes) {
			ISTYPE_MAP.put(isType.name(), isType);
		}
	}

	public static enum IsType {
		TRUE, FALSE, NULL, NOTNULL;
	}

	public static enum Operator {
		EQ, NE, LIKE, NOTLIKE, GT, GE, LT, LE, IN, NOTIN;
	}

	public static <T> Specification<T> newSpecification(Map<String, Object> map) {
		Specification<T> spec = new Specification<T>() {
			private static final long serialVersionUID = -1014760338170351857L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (map.containsKey("DISTINCT")) {
					query.distinct((boolean) map.get("DISTINCT"));
					map.remove("DISTINCT");
				}
				Set<String> keySet = map.keySet();
				for (String key : keySet) {
					// 处理OR
					if (key.contains(OR_SEPARATOR)) {
						List<Predicate> orPredicates = new ArrayList<>();
						String[] orSplit = key.split(OR_SEPARATOR);
						for (int i=0;i<orSplit.length;i++) {
							String[] strings = orSplit[i].split("_");
							if (SpecificationUtils.OPERATOR_MAP.containsKey(strings[0])) {
								Operator operator = Operator.valueOf(strings[0]);
								if (strings[0].contains(".")) {

								} else {
									if(map.get(key).getClass().isArray()) {
										handleOperator(root, criteriaBuilder, map, orPredicates, strings[1], operator,((Object[])map.get(key))[i]);
									}else {
										handleOperator(root, criteriaBuilder, map, orPredicates, strings[1], operator,map.get(key));
										
									}
								}
							} else if (strings[0].equals(SpecificationUtils.IS)) {
								if(map.get(key).getClass().isArray()) {
									IsType isType = IsType.valueOf((String) ((Object[])map.get(key))[i]);
									handleIsType(root, criteriaBuilder, map, predicates, strings[1], isType);
								}else {
									IsType isType = IsType.valueOf((String) map.get(key));
									handleIsType(root, criteriaBuilder, map, predicates, strings[1], isType);
								}
							}
						}
						predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
					}else {
						String[] strings = key.split("_");
						if (SpecificationUtils.OPERATOR_MAP.containsKey(strings[0])) {
							Operator operator = Operator.valueOf(strings[0]);
							if (strings[1].contains(".")) {
								handleOperatorJoin(root, criteriaBuilder, map, predicates, strings[1], operator,map.get(key));
							}else {
								handleOperator(root, criteriaBuilder, map, predicates, strings[1], operator,map.get(key));
							}
						}else if(strings[0].equals(SpecificationUtils.IS)){
							IsType isType = IsType.valueOf((String) map.get(key));
							handleIsType(root, criteriaBuilder, map, predicates, strings[1], isType);
							
						}
					}
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		return spec;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> void handleOperator(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, Object> map, List<Predicate> predicates, String fieldName, Operator operator,Object value) {
		switch (operator) {
		case EQ:
			predicates.add(criteriaBuilder.equal(root.get(fieldName), value));
			break;
		case NE:
			predicates.add(criteriaBuilder.notEqual(root.get(fieldName), value));
			break;
		case LIKE:
			predicates.add(criteriaBuilder.like(root.get(fieldName), "%"+value+"%"));
			break;
		case NOTLIKE:
			predicates.add(criteriaBuilder.notLike(root.get(fieldName), "%"+value+"%"));
			break;
		case GT:
			if (root.get(fieldName).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.greaterThan(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.greaterThan(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.greaterThan(root.get(fieldName), (Comparable) value));
			}
			break;
		case GE:
			if (root.get(fieldName).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), (Comparable) value));
			}
			break;
		case LT:
			if (root.get(fieldName).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.lessThan(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.lessThan(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.lessThan(root.get(fieldName), (Comparable) value));
			}
			break;
		case LE:
			if (root.get(fieldName).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), (Comparable) value));
			}
			break;
		case IN:
			In<Object> in = criteriaBuilder.in(root.get(fieldName));
			if (value.getClass().isArray()) {
				Object[] objArr = (Object[]) value;
				for (Object object : objArr) {
					in.value(object);
				}
				predicates.add(in);
			} else if (value instanceof String) {
				String str = (String) value;
				String[] split = str.split(",");
				for (String string2 : split) {
					in.value(string2);
				}
				predicates.add(in);
			} else if (value instanceof ArrayList) {
				ArrayList<?> list = (ArrayList<?>) value;
				for (Object object : list) {
					in.value(object);
				}
				predicates.add(in);
			} else {
				// TODO
			}
			break;
		case NOTIN:
			In<Object> in1 = criteriaBuilder.in(root.get(fieldName));
			if (value.getClass().isArray()) {
				Object[] objArr = (Object[]) value;
				for (Object object : objArr) {
					in1.value(object);
				}
				predicates.add(in1.not());
			} else if (value instanceof String) {
				String str = (String) value;
				String[] split = str.split(",");
				for (String string2 : split) {
					in1.value(string2);
				}
				predicates.add(in1.not());
			} else if (value instanceof ArrayList) {
				ArrayList<?> list = (ArrayList<?>) value;
				for (Object object : list) {
					in1.value(object);
				}
				predicates.add(in1.not());
			} else {
				// TODO
			}
			break;
		default:
			break;
		}

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> void handleOperatorJoin(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, Object> map, List<Predicate> predicates, String fieldName, Operator operator,Object value) {
		String[] strings = fieldName.split("\\.");
		Join<Object, Object> join = root.join(strings[0], JoinType.LEFT);
		switch (operator) {
		case EQ:
			predicates.add(criteriaBuilder.equal(join.get(strings[1]), value));
			break;
		case NE:
			predicates.add(criteriaBuilder.notEqual(join.get(strings[1]), value));
			break;
		case LIKE:
			predicates.add(criteriaBuilder.like(join.get(strings[1]), "%"+value+"%"));
			break;
		case NOTLIKE:
			predicates.add(criteriaBuilder.notLike(join.get(strings[1]), "%"+value+"%"));
			break;
		case GT:
			if (join.get(strings[1]).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.greaterThan(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.greaterThan(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.greaterThan(join.get(strings[1]), (Comparable) value));
			}
			break;
		case GE:
			if (join.get(strings[1]).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(join.get(strings[1]), (Comparable) value));
			}
			break;
		case LT:
			if (join.get(strings[1]).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.lessThan(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.lessThan(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.lessThan(join.get(strings[1]), (Comparable) value));
			}
			break;
		case LE:
			if (join.get(strings[1]).getJavaType().equals(Date.class)) {
				if (value instanceof String) {
					try {
						String dateString = (String) value;
						if (dateString.length() > 10) {
							predicates.add(criteriaBuilder.lessThanOrEqualTo(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATETIME_FORMAT)));
						} else {
							predicates.add(criteriaBuilder.lessThanOrEqualTo(join.get(strings[1]), SpecificationUtils.parseDate((String) value, SpecificationUtils.DATE_FORMAT)));
						}
					} catch (ParseException e) {
						e.printStackTrace();
						throw new RuntimeException("日期解析失败");
					}
				}
			} else {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(join.get(strings[1]), (Comparable) value));
			}
			break;
		case IN:
			In<Object> in = criteriaBuilder.in(join.get(strings[1]));
			if (value.getClass().isArray()) {
				Object[] objArr = (Object[]) value;
				for (Object object : objArr) {
					in.value(object);
				}
				predicates.add(in);
			} else if (value instanceof String) {
				String str = (String) value;
				String[] split = str.split(",");
				for (String string2 : split) {
					in.value(string2);
				}
				predicates.add(in);
			} else if (value instanceof ArrayList) {
				ArrayList<?> list = (ArrayList<?>) value;
				for (Object object : list) {
					in.value(object);
				}
				predicates.add(in);
			} else {
				// TODO
			}
			break;
		case NOTIN:
			In<Object> in1 = criteriaBuilder.in(join.get(strings[1]));
			if (value.getClass().isArray()) {
				Object[] objArr = (Object[]) value;
				for (Object object : objArr) {
					in1.value(object);
				}
				predicates.add(in1.not());
			} else if (value instanceof String) {
				String str = (String) value;
				String[] split = str.split(",");
				for (String string2 : split) {
					in1.value(string2);
				}
				predicates.add(in1.not());
			} else if (value instanceof ArrayList) {
				ArrayList<?> list = (ArrayList<?>) value;
				for (Object object : list) {
					in1.value(object);
				}
				predicates.add(in1.not());
			} else {
				// TODO
			}
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> void handleIsType(Root<T> root, CriteriaBuilder criteriaBuilder, Map<String, Object> map, List<Predicate> predicates, String fieldName, IsType isType) {
		Expression expression = null;
		if (fieldName.contains(".")) {
			String[] split = fieldName.split("\\.");
			Join<Object, Object> join = root.join(split[0], JoinType.LEFT);
			expression = join.get(split[1]);
		} else {
			expression = root.get(fieldName);
		}
		if(expression!=null) {
			switch (isType) {
			case NULL:
				predicates.add(criteriaBuilder.isNull(expression));
				break;
			case NOTNULL:
				predicates.add(criteriaBuilder.isNotNull(expression));
				break;
			case TRUE:
				predicates.add(criteriaBuilder.isTrue(expression));
				break;
			case FALSE:
				predicates.add(criteriaBuilder.isFalse(expression));
				break;
			default:
				break;
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> void handleOperator(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Map<String, Object> map, List<Predicate> predicates) {
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
			String[] strings = string.split("_");
			if (SpecificationUtils.OPERATOR_MAP.containsKey(strings[0])) {
				Operator operator = Operator.valueOf(strings[0]);
				switch (operator) {
				case EQ:
					predicates.add(criteriaBuilder.equal(root.get(strings[1]), map.get(string)));
					break;
				case NE:
					predicates.add(criteriaBuilder.notEqual(root.get(strings[1]), map.get(string)));
					break;
				case LIKE:
					predicates.add(criteriaBuilder.like(root.get(strings[1]), (String) map.get(string)));
					break;
				case NOTLIKE:
					predicates.add(criteriaBuilder.notLike(root.get(strings[1]), (String) map.get(string)));
					break;
				case GT:
					if (root.get(strings[1]).getJavaType().equals(Date.class)) {
						if (map.get(string) instanceof String) {
							try {
								String dateString = (String) map.get(string);
								if (dateString.length() > 10) {
									predicates.add(criteriaBuilder.greaterThan(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATETIME_FORMAT)));
								} else {
									predicates.add(criteriaBuilder.greaterThan(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATE_FORMAT)));
								}
							} catch (ParseException e) {
								e.printStackTrace();
								throw new RuntimeException("日期解析失败");
							}
						}
					} else {
						predicates.add(criteriaBuilder.greaterThan(root.get(strings[1]), (Comparable) map.get(string)));
					}
					break;
				case GE:
					if (root.get(strings[1]).getJavaType().equals(Date.class)) {
						if (map.get(string) instanceof String) {
							try {
								String dateString = (String) map.get(string);
								if (dateString.length() > 10) {
									predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATETIME_FORMAT)));
								} else {
									predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATE_FORMAT)));
								}
							} catch (ParseException e) {
								e.printStackTrace();
								throw new RuntimeException("日期解析失败");
							}
						}
					} else {
						predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(strings[1]), (Comparable) map.get(string)));
					}
					break;
				case LT:
					if (root.get(strings[1]).getJavaType().equals(Date.class)) {
						if (map.get(string) instanceof String) {
							try {
								String dateString = (String) map.get(string);
								if (dateString.length() > 10) {
									predicates.add(criteriaBuilder.lessThan(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATETIME_FORMAT)));
								} else {
									predicates.add(criteriaBuilder.lessThan(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATE_FORMAT)));
								}
							} catch (ParseException e) {
								e.printStackTrace();
								throw new RuntimeException("日期解析失败");
							}
						}
					} else {
						predicates.add(criteriaBuilder.lessThan(root.get(strings[1]), (Comparable) map.get(string)));
					}
					break;
				case LE:
					if (root.get(strings[1]).getJavaType().equals(Date.class)) {
						if (map.get(string) instanceof String) {
							try {
								String dateString = (String) map.get(string);
								if (dateString.length() > 10) {
									predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATETIME_FORMAT)));
								} else {
									predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(strings[1]), SpecificationUtils.parseDate((String) map.get(string), SpecificationUtils.DATE_FORMAT)));
								}
							} catch (ParseException e) {
								e.printStackTrace();
								throw new RuntimeException("日期解析失败");
							}
						}
					} else {
						predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(strings[1]), (Comparable) map.get(string)));
					}
					break;
				case IN:
					In<Object> in = criteriaBuilder.in(root.get(strings[1]));
					if (map.get(string).getClass().isArray()) {
						Object[] objArr = (Object[]) map.get(string);
						for (Object object : objArr) {
							in.value(object);
						}
						predicates.add(in);
					} else if (map.get(string) instanceof String) {
						String str = (String) map.get(string);
						String[] split = str.split(",");
						for (String string2 : split) {
							in.value(string2);
						}
						predicates.add(in);
					} else if (map.get(string) instanceof ArrayList) {
						ArrayList<?> list = (ArrayList<?>) map.get(string);
						for (Object object : list) {
							in.value(object);
						}
						predicates.add(in);
					} else {
						// TODO
					}
				default:
					break;
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> void handleIsType(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, Map<String, Object> map, List<Predicate> predicates) {
		Expression expression = null;
		Set<String> keySet = map.keySet();
		for (String string : keySet) {
			String[] strings = string.split("_");
			if (strings[0].equals(SpecificationUtils.IS)) {
				if (strings[1].contains(".")) {
					String[] split = strings[1].split(".");
					Join<Object, Object> join = root.join(split[0], JoinType.LEFT);
					expression = join.get(split[1]);
				} else {
					expression = root.get(strings[1]);
				}
				IsType isType = IsType.valueOf((String) map.get(string));

				switch (isType) {
				case NULL:
					predicates.add(criteriaBuilder.isNull(expression));
					break;
				case NOTNULL:
					predicates.add(criteriaBuilder.isNotNull(expression));
					break;
				case TRUE:
					predicates.add(criteriaBuilder.isTrue(expression));
					break;
				case FALSE:
					predicates.add(criteriaBuilder.isFalse(expression));
					break;
				default:
					break;
				}
			}
		}
	}

	private static Date parseDate(String str, String format) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(str);
		return date;
	}

}
