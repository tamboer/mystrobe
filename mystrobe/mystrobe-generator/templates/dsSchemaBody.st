@Generated
public $name$() {
	$if(id)$	id = "$id$";$endif$
	$if(batchSize)$	batchSize = $batchSize$;$endif$
	$if(margin)$	margin = $margin$;$endif$
	$if(urn)$	urn = "$urn$";$endif$
	$if(hasAtomicChanges)$	hasAtomicChanges = $hasAtomicChanges$;$endif$
	$if(hasFilteredChildren)$	hasFilteredChildren = $hasFilteredChildren$;$endif$
	$if(isDynamic)$	isDynamic = $isDynamic$;$endif$
	$if(isReadOnly)$ isReadOnly = $isReadOnly$;$endif$

	loadRelations(new String[] {$relations$});

	$daoMap:{s|daoMap.put($s$);}; separator="\n"$
}