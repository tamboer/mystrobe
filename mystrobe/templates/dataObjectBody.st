@Generated
private static final long serialVersionUID = $serialVersionUID$L;
	
@Generated
@Deprecated
public $className$ (IAppConnector connector) {
	super(connector, new $dsSchema$(), new $daoClassName$());
}

@Generated
public $className$ (IConfig config, String appName) {
	super(config, appName, new $dsSchema$(), new $daoClassName$());
}

@Generated
@Deprecated
public $className$ (IAppConnector connector, IDSSchema dsSchema, IDAOSchema$if(extendsGenerics)$<$extendsGenerics$>$endif$ daoSchema ) {
	super(connector, dsSchema, daoSchema);
}

@Generated
public $className$ (IConfig config, String appName, IDSSchema dsSchema, IDAOSchema$if(extendsGenerics)$<$extendsGenerics$>$endif$ daoSchema ) {
	super(config, appName, dsSchema, daoSchema);
}
