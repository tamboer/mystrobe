package $packageName$;

$import:{s|import $s$;}; separator="\n"$

$comment$

$annotation$
public class $className$ $if(extends)$extends $extends$$endif$$if(extendsGenerics)$<$extendsGenerics$>$endif$ $if(implements)$implements $implements$ $endif${

	$body$
	
	$userMainClassCode$
	
}

$userAfterMainClassCode$

