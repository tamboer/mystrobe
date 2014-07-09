@Generated
private static final long serialVersionUID = $serialVersionUID$L;

$fields:{f|$f$}; separator="\n"$

@Generated
public static Class<? extends IDSSchema> getDSSchemaClass() {
	return $dsSchemaClass$.class;
}

@Generated	
public static Class<? extends DAOSchema<$className$>> getDAOSchemaClass() {
	return $daoSchemaClass$.class;
}

$methods:{m|$m$}; separator="\n\n"$

@Generated
public String getRowId() {
	return $rowid$;
}

@Override
public String toString() {
	return $toString$;
}
