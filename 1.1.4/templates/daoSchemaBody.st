@Generated
public enum Cols {
	
	$columnsEnum:{p|$p$}; separator=",\n"$
	;

	private String columnName;
		
	private Cols(String columnName) {
		this.columnName = columnName;
	}

	public String id() {
		return columnName;
	}	

	public String toString() {
		return columnName;
	}
}

@Generated
public $name$() {
	assignValues();
}

@Generated
protected void assignValues() {
	super.assignValues();
	$if(daoId)$	daoId = "$daoId$";$endif$
	$if(iDataTypeClass)$	iDataTypeClass = $iDataTypeClass$;$endif$
	$if(batchSize)$	batchSize = $batchSize$;$endif$
	$if(margin)$	margin = $margin$;$endif$
	$if(isAutosync)$	isAutosync = $isAutosync$;$endif$
	$if(isOpenOnInit)$	isOpenOnInit = $isOpenOnInit$;$endif$
	$if(isReadOnly)$	isReadOnly = $isReadOnly$;$endif$
	$if(isDynamic)$	isDynamic = $isDynamic$;$endif$
	$if(isSendChangesOnly)$	isSendChangesOnly = $isSendChangesOnly$;$endif$
	$if(isSetFilterEveryTime)$	isSetFilterEveryTime = $isSetFilterEveryTime$;$endif$

	properties = new LinkedHashMap<String, Map<SchemaColumnProperties, String>>();

	$properties:{p|$p$}; separator="\n\n"$
}