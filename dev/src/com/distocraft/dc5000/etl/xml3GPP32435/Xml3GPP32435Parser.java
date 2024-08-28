/*
 * Created on 3.1.2008
 *
 */
package com.distocraft.dc5000.etl.xml3GPP32435;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.distocraft.dc5000.etl.parser.Main;
import com.distocraft.dc5000.etl.parser.MeasurementFile;
import com.distocraft.dc5000.etl.parser.Parser;
import com.distocraft.dc5000.etl.parser.SourceFile;
import com.distocraft.dc5000.repository.cache.DFormat;
import com.distocraft.dc5000.repository.cache.DItem;
import com.distocraft.dc5000.repository.cache.DataFormatCache;
import com.ericsson.eniq.common.ENIQEntityResolver;

/**
 * 3GPP TS 32.435 Parser <br>
 * 
 * <br>
 * Configuration: <br>
 * <br>
 * Database usage: Not directly <br>
 * <br>
 * <br>
 * Version supported: v 7.20 <br>
 * <br>
 * Copyright Ericsson 2008 <br>
 * <br>
 * $id$ <br>
 * 
 * <br>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <tr bgcolor="#CCCCFF" class="TableHeasingColor">
 * <td colspan="4"><font size="+2"><b>Parameter Summary</b></font></td>
 * </tr>
 * <tr>
 * <td><b>Name</b></td>
 * <td><b>Key</b></td>
 * <td><b>Description</b></td>
 * <td><b>Default</b></td>
 * </tr>
 * <tr>
 * <td>Vendor ID mask</td>
 * <td>3GPP32435Parser.vendorIDMask</td>
 * <td>Defines how to parse the vendorID</td>
 * <td>.+,(.+)=.+</td>
 * </tr>
 * <tr>
 * <td>Vendor ID from</td>
 * <td>3GPP32435Parser.readVendorIDFrom</td>
 * <td>Defines where to parse vendor ID (file/data supported)<br /> 
 * <td>data</td>
 * </tr>
 * <tr>
 * <td>Fill empty MOID</td>
 * <td>3GPP32435Parser.FillEmptyMOID</td>
 * <td>Defines whether empty moid is filled or not (true/ false)</td>
 * <td>true</td>
 * </tr>
 * <tr>
 * <td>Fill empty MOID style</td>
 * <td>3GPP32435Parser.FillEmptyMOIDStyle</td>
 * <td>Defines the style how moid is filled (static/inc supported)</td>
 * <td>inc</td>
 * </tr>
 * <tr>
 * <td>Fill empty MOID value</td>
 * <td>3GPP32435Parser.FillEmptyMOIDValue</td>
 * <td>Defines the value for the moid that is filled</td>
 * <td>0</td>
 * </tr>
 * </table>
 * <br>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0">
 * <tr bgcolor="#CCCCFF" class="TableHeasingColor">
 * <td colspan="2"><font size="+2"><b>Added DataColumns</b></font></td>
 * </tr>
 * <tr>
 * <td><b>Column name</b></td>
 * <td><b>Description</b></td>
 * </tr>
 * <tr>
 * <td>collectionBeginTime</td>
 * <td>contains the begin time of the whole collection</td>
 * </tr>
 * <tr>
 * <td>objectClass</td>
 * <td>contains the vendor id parsed from MOID</td>
 * </tr>
 * <tr>
 * <td>MOID</td>
 * <td>contains the measured object id</td>
 * </tr>
 * <tr>
 * <td>filename</td>
 * <td>contains the filename of the inputdatafile.</td>
 * </tr>
 * <tr>
 * <td>PERIOD_DURATION</td>
 * <td>contains the parsed duration of this measurement</td>
 * </tr>
 * <tr>
 * <td>DATETIME_ID</td>
 * <td>contains the counted starttime of this measurement</td>
 * </tr>
 * <tr>
 * <td>DC_SUSPECTFLAG</td>
 * <td>contains the suspected flag value</td>
 * </tr>
 * <tr>
 * <td>DIRNAME</td>
 * <td>Contains full path to the inputdatafile.</td>
 * </tr>
 * <tr>
 * <td>JVM_TIMEZONE</td>
 * <td>contains the JVM timezone (example. +0200)</td>
 * </tr>
 * <tr>
 * <td>vendorName</td>
 * <td>contains the vendor name</td>
 * </tr>
 * <tr>
 * <td>fileFormatVersion</td>
 * <td>contains the version of file format</td>
 * </tr>
 * <tr>
 * <td>dnPrefix</td>
 * <td>contains the dn prefix</td>
 * </tr>
 * <tr>
 * <td>localDn</td>
 * <td>contains the local dn</td>run
 * </tr>
 * <tr>
 * <td>managedElementLocalDn</td>
 * <td>contains the local dn of managedElement element</td>
 * </tr>
 * <tr>
 * <td>elementType</td>
 * <td>contains the element type</td>
 * </tr>
 * <tr>
 * <td>userLabel</td>
 * <td>contains the user label</td>
 * </tr>
 * <tr>
 * <td>swVersion</td>
 * <td>contains the software version</td>
 * </tr>
 * <tr>
 * <td>endTime</td>
 * <td>contains the granularity period end time</td>
 * </tr>
 * <tr>
 * <td>measInfoId</td>
 * <td>contains the measInfoId</td>
 * </tr>
 * <tr>
 * <td>jobId</td>
 * <td>contains the jobId</td>
 * </tr>
 * <tr>
 * <td>&lt;measType&gt; (amount varies based on measurement executed)</td>
 * <td>&lt;measValue&gt; (amount varies based on measurement executed)</td>
 * </tr>
 * </table>
 * <br>
 * <br>
 * 
 * @author pylkk?nen <br>
 * <br>
 * 
 */
public class Xml3GPP32435Parser extends DefaultHandler implements Parser {

	// Virtual machine timezone unlikely changes during execution of JVM
	private static final String JVM_TIMEZONE = (new SimpleDateFormat("Z"))
			.format(new Date());
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");

	/**
	 * 3GPP 7.20
	 */
	private String fileFormatVersion;
	private String vendorName;
	private String dnPrefix;
	private String fsLocalDN;
	private String elementType;
	private String meLocalDN;
	private String userLabel;
	private String swVersion;
	private String collectionBeginTime;
	// private String collectionEndTime; //received so late, that migth not be
	// used
	private String granularityPeriodDuration;
	private String granularityPeriodEndTime;
	private String repPeriodDuration;
	private String jobId;
	private String measInfoId;
	private HashMap measNameMap;
	private HashMap clusterMap;

	// Since special handling of #-mark causes parsing to fail in some cases,
	// let's keep the "original" counterrnames in the map also without special
	// #-handling
	private HashMap<String, String> origMeasNameMap;
	private HashMap<String, Integer> occuranceMap;

	private String suspectFlag = "";
	private String measIndex;
	private String measValueIndex;
	private String measObjLdn;

	private String charValue;

	private SourceFile sourceFile;

	private String objectClass;

	private String objectMask;

	private String matchObjectMaskAgainst;

	private String readVendorIDFrom;

	private boolean fillEmptyMoid = true;
	private boolean containsFilter = false;
	private String fillEmptyMoidStyle = "";

	private String fillEmptyMoidValue = "";
	
	private String objectClassPostFix = "";

	private String oldObjClass;

	private MeasurementFile measFile = null;

	private MeasurementFile vectorMeasFile = null;

	private Logger log;

	private String techPack;

	private String setType;

	private String setName;

	private int status = 0;

	private Main mainParserObject = null;

	private String workerName = "";

	final private List errorList = new ArrayList();

	private boolean hashData = false;

	private boolean dataFormatNotFound = false;

	private String interfaceName = "";

	private HashMap<String, HashMap<String, HashMap<String, String>>> loaderClass;

	private HashMap<String, HashMap<String, String>> moidMap;

	private HashMap<String, String> counterMap;

	private HashMap<String, String> objectMap;

	private HashMap<String, String> counterValueMap;

	private HashMap<String, HashMap<String, String>> dataMap;

	private Map<String, Map<String, String>> vectorBinValueMap = new HashMap<String, Map<String, String>>();

	private HashMap<String, Map<String, Map<String, String>>> vectorData = new HashMap<String, Map<String, Map<String, String>>>();
	
	private HashMap<String, String> measInfoAndLdn = new HashMap<String, String>();

	private String rangeCounterTag = "VECTOR";

	private String cmVectorTag = "CMVECTOR";

	private String rangeColunName = ""; // name of the range column

	private String rangePostfix = ""; // string that is added to the

	private String compressedVectorTag = "COMPRESSEDVECTOR";

	private Set compressedVectorCounters;
	private Set<String> flexCompVectors;

	private String keyColumnTag = "KEY";
	 
	private String vectorPostfix = null;
	
	private Map measurement;
	private Map vectorMeasurement;
	private Integer multiDynMaskCount;
	private List<String> multiDynMaskConfig;

	private static  final String DELIMITER = ",";

	private String measInfoIdPattern;

	DataFormatCache dfc = DataFormatCache.getCache();

	// For L17A Differentiated Observability Flex Counter handling
	private String flexPostfix = null; // Post-fix to MOID for Flex tables
	/*MyCodeStart*/
	private String flexVectorPostfix = null; // Post-fix to MOID for Flex Vector tables
	private boolean hasFlexVectorCounters = false;
	/*MyCodeEnd*/
	private String flexdynPostfix = null;
	private boolean hasFlexDynCounters = false;
	private boolean hasFlexCounters = false; // If flex counters not required
	private boolean createOwnFlexFile = true; // To keep it consistent with
												// vector implementation/future
												// impact
	
	
	private MeasurementFile flexMeasFile = null;
	private String flexCounterTag; // Tag to differentiate flex counters in
									// techpack
	/*MyCodeStart*/
	private String flexVectorTag; // Tag to differentiate flex counters in
	// techpack
	
	private String flexCompVectorTag; // Tag to differentiate flex counters in
	// techpack
	/*MyCodeEnd*/
	private String flexDynTag;
	private HashMap flexValueMap; // Map to store flex counter values
	private HashMap <String, Map> flexFilterMap; // Map to arrange data per filter
	private HashMap flexMoidMap; // Map to arrange data per MOID
	private Map flexCounterBin; // Map to store list of Flex counters in an MOID
								// tag
	//Fields for handling flex and dynamic counters
	
	private Map<String, Object> flexDynCounterBin;
	
	//Fields for handling dynamic counters
	private String dynPostfix = null; 
	private boolean hasDynCounters = false; 
	private boolean createOwnDynFile = true;
	private MeasurementFile dynMeasFile = null;
	private String dynCounterTag;
	private Map<String, Map<String,String>> dynIndexMap; 
	private Map<String, Map<String, Map<String,String>>> dynMoidMap; 
	private Map<String, Object> dynCounterBin;
	private String dynMask;
	private String flexDynMask;
	private int dynPartIndex;
	private int dynIndexIndex;
	private Map<String, Map<String,String>> flexDynIndexMap;
	private MeasurementFile flexDynMeasFile = null;
	private boolean createOwnFlexDynFile = true;
	private Map<String, Map<String, Map<String,String>>> flexDynMoidMap;
	List<Integer> dynPartIndexList = new ArrayList<>();
	List<Integer> dynIndexIndexList = new ArrayList<>();
	private int staticPartIndex;
	private long parseStartTime;
	private long fileSize = 0L;
	private long totalParseTime = 0L;
	private int fileCount = 0;
	private Set<String> measInfoPrefixSet;
	private boolean isPrefixMeasInfo = false;
	private static final String FLEX_COUNTER_PATTERN = "(.+?)_(.*)";
	private static final String FILTER_HASH = "FLEX_FILTERHASHINDEX";
	private static final String FLEX_FILTERNAME = "FLEX_FILTERNAME";
	private static final String DN = "Do nothing!!!";
	private static final String FLEX_RANGE_COUNTERS = "flexRangeCounters";
	private HashMap<String, String> measTypeMap=new HashMap<String,String>();
	private static final String VECTOR_NO_DATA  = "Vector {} is not having valid data.";
	private static final String PARSER_FAILED_EXCEPTION = "Worker parser failed to exception";
	private static final String ERROR_SAVE_MEAS_DATA = "Error saving measurement data: ";
	private static final String ERROR_OPEN_MEAS_DATA = "Error opening measurement data: ";
	private static final String ERROR_CLOSE_MEAS_FILE = "Error closing measurement file";
	private static final String DATA_MORE_R_THAN_MT = "Data contains one or more r-tags than mt-tags";
	private static final String VENDOR_NAME  = "vendorName";
	private static final String USER_LABEL = "userLabel";
	private static final String SW_VERSION = "swVersion";
	private static final String MEAS_OBJ_LDN = "measObjLdn";
	private static final String MEAS_INFO_ID = "measInfoId";
	private static final String LOCAL_DN = "localDn";
	private static final String KEY_COLUMNS = "keyColumns";
	private static final String JOB_ID = "jobId";
	private static final String FLEX_COUNTERS = "flexCounters";
	private static final String FLEX_DYN_COUNTERS = "flexDynCounters";
	private static final String FILE_NAME = "filename";
	private static final String FILE_FORMAT_VERSION = "fileFormatVersion";
	private static final String FALSE = "false";
	private static final String END_TIME = "endTime";
	private static final String ELEMENT_TYPE = "elementType";
	private static final String DUMMY_FILE = "dummyfile";
	private static final String DUMMY_DIR = "dummydir";
	private static final String DN_PREFIX = "dnPrefix";
	private static final String JVM_TIME_ZONE = "JVM_TIMEZONE";
	private static final String DIR_NAME = "DIRNAME";
	private static final String DC_SUSPECT_FLAG = "DC_SUSPECTFLAG";
	private static final String DC_VECTOR_INDEX = "DCVECTOR_INDEX";
	
	/**
   * 
   */
	@Override
	public void init(final Main main, final String techPack,
			final String setType, final String setName, final String workerName) {
		this.mainParserObject = main;
		this.techPack = techPack;
		this.setType = setType;
		this.setName = setName;
		this.status = 1;
		this.workerName = workerName;

		String logWorkerName = "";
		if (workerName.length() > 0) {
			logWorkerName = "." + workerName;
		}

		log = Logger.getLogger("etl." + techPack + "." + setType + "."
				+ setName + ".parser.xml3GPP32435Parser" + logWorkerName);

	}

	@Override
	public int status() {
		return status;
	}

	public List errors() {
		return errorList;
	}

	@Override
	public void run() {
		try {
			this.status = 2;
			SourceFile sf = null;
			parseStartTime = System.currentTimeMillis();

			while ((sf = mainParserObject.nextSourceFile()) != null) {
				try {
					fileCount++;
					fileSize += sf.fileSize();
					mainParserObject.preParse(sf);
					parse(sf, techPack, setType, setName);
					mainParserObject.postParse(sf);
				} catch (final Exception e) {
					mainParserObject.errorParse(e, sf);
				} finally {
					mainParserObject.finallyParse(sf);
				}
			}
			totalParseTime = System.currentTimeMillis() - parseStartTime;
			if (totalParseTime != 0) {
				/*
				 * Please don't make any changes to the below logger.
				 * It is used as reference for some other process.
				 */
				
				log.log(Level.INFO, () ->"Parsing Performance :: " + fileCount
						+ " files parsed in " + totalParseTime
						+ " ms, filesize is " + fileSize
						+ " bytes and throughput : "
						+ (fileSize / totalParseTime) + " bytes/ms.");
			}
		} catch (final Exception e) {
			// Exception catched at top level. No good.
			log.log(Level.WARNING,() -> PARSER_FAILED_EXCEPTION + e);
			errorList.add(e);
		} finally {
			this.status = 3;
		}
	}

	/**
   * 
   */
	@Override
	public void parse(final SourceFile sf, final String techPack,
			final String setType, final String setName) throws Exception {
		log.finest("Entered 1st Parse ");
		if (measFile != null) {
			try {
				log.log(Level.FINEST,"Closing Meas File");
				measFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() -> PARSER_FAILED_EXCEPTION + e);
				throw new SAXException(ERROR_CLOSE_MEAS_FILE);
			}
		}
		if (vectorMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing vectorMeasFile File");
				vectorMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST, () -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException(ERROR_CLOSE_MEAS_FILE);
			}

		}
		if (flexMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing flexMeasFile File");
				flexMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST, () -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException("Error closing flex measurement file");
			}

		}
		
		if (dynMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing dynMeasFile File");
				dynMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() ->" PARSER_FAILED_EXCEPTION" +e);
				throw new SAXException("Error closing dyn measurement file");
			}

		}
		if (flexDynMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing flexDynMeasFile File");
				flexDynMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() ->" PARSER_FAILED_EXCEPTION" +e);
				throw new SAXException("Error closing flexdyn measurement file");
			}

		}
		log.log(Level.FINEST,() ->"CA : measFile " +measFile);
		log.log(Level.FINEST,() ->"CA : vectorMeasFile " +vectorMeasFile);
		log.log(Level.FINEST,() ->"CA : flexMeasFile " +flexMeasFile);
		log.log(Level.FINEST,() ->"CA : dynMeasFile " +dynMeasFile);
		log.log(Level.FINEST,() ->"CA : flexDynMeasFile " +flexDynMeasFile);
		
		this.measFile = null;
		final long start = System.currentTimeMillis();
		this.sourceFile = sf;
		objectMask = sf.getProperty("x3GPPParser.vendorIDMask", ".+,(.+)=.+");
		measInfoIdPattern = sf.getProperty("x3GPPParser.vendorIDMask", "(.*)");
		matchObjectMaskAgainst = sf.getProperty(
				"x3GPPParser.matchVendorIDMaskAgainst", "subset");
		readVendorIDFrom = sf.getProperty("x3GPPParser.readVendorIDFrom",
				sf.getProperty("3GPP32435Parser.readVendorIDFrom", null));
		if ((null == readVendorIDFrom) || (readVendorIDFrom .equals(""))) {
			readVendorIDFrom = sf.getProperty("x3GPPParser.readVendorIDFrom",
					"data");
		}
		log.log(Level.FINEST,() ->"VALUE OF READVENDORIDFROM IS: " +readVendorIDFrom);
		
		fillEmptyMoid = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.FillEmptyMOID", "true"));
		fillEmptyMoidStyle = sf.getProperty("x3GPPParser.FillEmptyMOIDStyle",
				"inc");
		fillEmptyMoidValue = sf.getProperty("x3GPPParser.FillEmptyMOIDValue",
				"0");
		hashData = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.HashData",
				sf.getProperty("3GPP32435Parser.HashData", FALSE)));
		interfaceName = sf.getProperty("interfaceName", "");
		rangePostfix = sf.getProperty("x3GPPParser.RangePostfix",
				sf.getProperty("3GPP32435Parser.RangePostfix", "_DCVECTOR"));
		rangeColunName = sf.getProperty("x3GPPParser.RangeColumnName", sf
				.getProperty("3GPP32435Parser.RangeColumnName",
						DC_VECTOR_INDEX));
		rangeCounterTag = sf.getProperty("x3GPPParser.RangeCounterTag",
				sf.getProperty("3GPP32435Parser.RangeCounterTag", "VECTOR"));
		cmVectorTag = sf.getProperty("x3GPPParser.cmVectorTag",
				sf.getProperty("3GPP32435Parser.cmVectorTag", "CMVECTOR"));

		compressedVectorTag = sf.getProperty("x3GPPParser.compressedVectorTag",
				sf.getProperty("3GPP32435Parser.compressedVectorTag",
						"COMPRESSEDVECTOR"));
		compressedVectorCounters = new HashSet();
		keyColumnTag = sf.getProperty("x3GPPParser.KeyColumnTag",
				sf.getProperty("3GPP32435Parser.KeyColumnTag", "KEY"));
	
		vectorPostfix = sf.getProperty("x3GPPParser.VectorPostfix",
				sf.getProperty("3GPP32435Parser.VectorPostfix", "_V"));

		// Flex counter initializations before parse
		flexPostfix = sf.getProperty("x3GPPParser.flexPostfix", "_FLEX");
		/*MyCodeStart*/
		flexVectorPostfix = sf.getProperty("x3GPPParser.flexVectorPostfix",
				"_FLEX_V");
		hasFlexVectorCounters = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.hasFlexCounters", FALSE));
		/*MyCodeEnd*/
		flexdynPostfix = sf.getProperty("x3GPPParser.multiDynPostfix", "_FLEX_DYN");
		hasFlexDynCounters = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.hasMultiDynCounters", FALSE));
		hasFlexCounters = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.hasFlexCounters", FALSE));
		
		createOwnFlexFile = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.createOwnFlexFile", FALSE));
		flexCounterTag = sf.getProperty("x3GPPParser.flexCounterTag",
				"FlexCounter");
		
		/*MyCodeStart*/
		flexVectorTag = sf.getProperty("x3GPPParser.flexVectorTag",
				"FlexVector");
		flexCompVectorTag = sf.getProperty("x3GPPParser.flexCompVectorTag",
				"FlexCompVector");
		/*MyCodeEnd*/
		
		flexDynTag = sf.getProperty("x3GPPParser.multiDynCounterTag","MultiDynCounter");
		if(null != sf.getProperty("x3GPPParser.multiDynMaskCount"))
			multiDynMaskCount =Integer.valueOf(sf.getProperty("x3GPPParser.multiDynMaskCount"));
		else
			multiDynMaskCount = 0;
		log.log(Level.FINEST,()->"multiDynMaskCount is: "+multiDynMaskCount);
			multiDynMaskConfig = new ArrayList<>();
		for(int i=1;i <= multiDynMaskCount;i++ ) {
			multiDynMaskConfig.add(sf.getProperty("x3GPPParser.multiDynMaskConfig."+i));
		}
		dynPostfix = sf.getProperty("x3GPPParser.dynPostfix", "_DYN");
		hasDynCounters = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.hasDynCounters", FALSE));
		createOwnDynFile = "true".equalsIgnoreCase(sf.getProperty(
				"x3GPPParser.createOwnDynFile", FALSE));
		dynCounterTag = sf.getProperty("x3GPPParser.dynCounterTag",
				"DynCounter");
		dynMask = sf.getProperty("x3GPPParser.dynMask",
				"(queue)(.+?)_(.+)");
		
		objectClassPostFix = sf.getProperty("x3GPPParser.objectClassPostFix", "");
		
		try {
			dynPartIndex = Integer.parseInt(sf.getProperty("x3GPPParser.dynPartIndex","1"));
		} catch (Exception e) {
			log.log(Level.WARNING, "Not able to parse x3GPPParser.dynPartIndex, "
					+ "hence assigning the default value '1'",e );
			dynPartIndex = 1;
		}
		try {
			dynIndexIndex = Integer.parseInt(sf.getProperty("x3GPPParser.dynIndexIndex","2"));
		} catch (Exception e) {
			log.log(Level.WARNING, "Not able to parse x3GPPParser.dynIndexIndex, "
					+ "hence assigning the default value '2'",e );
			dynIndexIndex = 2;
		}
		try {
			staticPartIndex = Integer.parseInt(sf.getProperty("x3GPPParser.staticPartIndex","3"));
		} catch (Exception e) {
			log.log(Level.WARNING, "Not able to parse x3GPPParser.staticPartIndex, "
					+ "hence assigning the default value '3'",e );
			staticPartIndex = 3;
		}
		
		
		final String addVendorIDToDelimiter=sf.getProperty("addVendorIDToDelimiter",",");
		log.log(Level.FINEST,() ->"addVendorIDToDelimiter value  is " +addVendorIDToDelimiter);
		
		final String addMeasInfoPrefix = sf.getProperty("addVendorIDTo", "");
		measInfoPrefixSet = new HashSet<>();

		if (addMeasInfoPrefix != null && !("").equals(addMeasInfoPrefix)) {
			final String[] addMeasInfoPrefixs = addMeasInfoPrefix.split(addVendorIDToDelimiter);
			for (int i = 0; i < addMeasInfoPrefixs.length; i++) {
				measInfoPrefixSet.add(addMeasInfoPrefixs[i]);
			}
		}
		log.log(Level.FINEST,() ->"CA : objectMask " +objectMask);
		log.log(Level.FINEST,() ->"CA : measInfoIdPattern " +measInfoIdPattern);
		log.log(Level.FINEST,() ->"CA : matchObjectMaskAgainst " +matchObjectMaskAgainst);
		
		log.log(Level.FINEST,() ->"CA : fillEmptyMoid " +fillEmptyMoid);
		log.log(Level.FINEST,() ->"CA : fillEmptyMoidStyle " +fillEmptyMoidStyle);
		log.log(Level.FINEST,() ->"CA : fillEmptyMoidValue " +fillEmptyMoidValue);
		log.log(Level.FINEST,() ->"CA : hashData " +hashData);
		log.log(Level.FINEST,() ->"CA : interfaceName " +interfaceName);
		log.log(Level.FINEST,() ->"CA : rangePostfix " +rangePostfix);
		log.log(Level.FINEST,() ->"CA : rangeColunName " +rangeColunName);
		log.log(Level.FINEST,() ->"CA : rangeCounterTag " +rangeCounterTag);
		log.log(Level.FINEST,() ->"CA : cmVectorTag " +cmVectorTag);
		log.log(Level.FINEST,() ->"CA : compressedVectorTag " +compressedVectorTag);
		log.log(Level.FINEST,() ->"CA : keyColumnTag " +keyColumnTag);
		
		log.log(Level.FINEST,() ->"CA : flexPostfix " +flexPostfix);
		/*MyCodeStart*/
		log.log(Level.FINEST,() ->"CA : vectorPostfix " +vectorPostfix);
		log.log(Level.FINEST,() ->"CA : flexVectorPostfix " +flexVectorPostfix);
		log.log(Level.FINEST,() ->"CA : hasFlexVectorCounters " +hasFlexVectorCounters);
		/*MyCodeEnd*/
		log.log(Level.FINEST,() ->"CA : flexdynPostfix " +flexdynPostfix);
		log.log(Level.FINEST,() ->"CA : hasFlexDynCounters " +hasFlexDynCounters);
		log.log(Level.FINEST,() ->"CA : hasFlexCounters " +hasFlexCounters);
		log.log(Level.FINEST,() ->"CA : createOwnFlexFile " +createOwnFlexFile);
		log.log(Level.FINEST,() ->"CA : flexCounterTag " +flexCounterTag);
		/*MyCodeStart*/
		log.log(Level.FINEST,() ->"CA : flexVectorTag " +flexVectorTag);
		log.log(Level.FINEST,() ->"CA : flexCompVectorTag " +flexCompVectorTag);
		/*MyCodeEnd*/
		log.log(Level.FINEST,() ->"CA : flexDynTag " +flexDynTag);
		log.log(Level.FINEST,() ->"CA : dynPostfix " +dynPostfix);
		log.log(Level.FINEST,() ->"CA : hasDynCounters " +hasDynCounters);
		log.log(Level.FINEST,() ->"CA : createOwnDynFile " +createOwnDynFile);
		log.log(Level.FINEST,() ->"CA : createOwnFlexDynFile " +createOwnFlexDynFile);
		log.log(Level.FINEST,() ->"CA : dynCounterTag " +dynCounterTag);
		log.log(Level.FINEST,() ->"CA : dynMask " +dynMask);
		log.log(Level.FINEST,() ->"CA : flexDynMask " +flexDynMask);
		log.log(Level.FINEST,() ->"CA : objectClassPostFix " +objectClassPostFix);
		log.log(Level.FINEST,() ->"CA : dynPartIndex " +dynPartIndex);
		log.log(Level.FINEST,() ->"CA : dynIndexIndex " +dynIndexIndex);
		log.log(Level.FINEST,() ->"CA : staticPartIndex " +staticPartIndex);
		log.log(Level.FINEST,() ->"CA : addMeasInfoPrefix " +addMeasInfoPrefix);
		log.log(Level.FINEST,() ->"CA : measInfoPrefixSet " +measInfoPrefixSet);
		final SAXParserFactory spf = SAXParserFactory.newInstance();

		final SAXParser parser = spf.newSAXParser();
		final XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.setErrorHandler(this);

		xmlReader.setEntityResolver(new ENIQEntityResolver(log.getName()));
		final long middle = System.currentTimeMillis();
		xmlReader.parse(new InputSource(sourceFile.getFileInputStream()));
		final long end = System.currentTimeMillis();
		log.log(Level.FINER,() -> "Data parsed. Parser initialization took " +(middle - start)+" ms"); 
		log.log(Level.FINER,() ->" parsing {}  ms."+(end - middle));
		log.log(Level.FINER,() ->" Total: {}  ms." + (end - start));
		oldObjClass = null;
	}
	
	private Matcher getMatcher(String input, String pattern){
		Pattern p = Pattern.compile(pattern);
		return p.matcher(input);
	}

	/**
   * 
   */
	public void parse(final FileInputStream fis) throws Exception {
		log.finest("Entered 2nd Parse ");
		final long start = System.currentTimeMillis();
		final SAXParserFactory spf = SAXParserFactory.newInstance();
		
		final SAXParser parser = spf.newSAXParser();
		final XMLReader xmlReader = parser.getXMLReader();
		xmlReader.setContentHandler(this);
		xmlReader.setErrorHandler(this);
		final long middle = System.currentTimeMillis();
		xmlReader.parse(new InputSource(fis));
		final long end = System.currentTimeMillis();
		log.log(Level.FINEST,() -> "Data parsed. Parser initialization took {}  ms"
				+(middle - start));
		log.log(Level.FINEST,() ->"parsing {}  ms. " +(end - middle));
		log.log(Level.FINEST,() ->"Total: {}  ms." +(end - start));
	}

	public HashMap strToMap(final String str) {

		final HashMap hm = new HashMap();
		int index = 0;
		if (str != null) {

			// list all triggers
			final StringTokenizer triggerTokens = new StringTokenizer(str, " ");
			while (triggerTokens.hasMoreTokens()) {
				index++;
				hm.put("" + index, triggerTokens.nextToken());
			}
		}

		return hm;
	}

	

	/**
	 * Event handlers
	 */
	@Override
	public void startDocument() {
		// Do nothing because of X and Y.
	}

	@Override
	public void endDocument() throws SAXException {

		if (measFile != null) {
			try {
				log.log(Level.FINEST,"Closing Meas File");
				measFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException(ERROR_CLOSE_MEAS_FILE);
			}
		}
		if (vectorMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing vectorMeasFile File");
				vectorMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException(ERROR_CLOSE_MEAS_FILE);
			}

		}
		if (flexMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing flexMeasFile File");
				flexMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException("Error closing flex measurement file");
			}

		}
		
		if (dynMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing dynMeasFile File");
				dynMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException("Error closing dyn measurement file");
			}

		}
		if (flexDynMeasFile != null) {
			try {
				log.log(Level.FINEST,"Closing flexDynMeasFile File");
				flexDynMeasFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() -> PARSER_FAILED_EXCEPTION +e);
				throw new SAXException("Error closing flexdyn measurement file");
			}

		}

	}

	@Override
	public void startElement(final String uri, final String name,
			final String qName, final Attributes atts) throws SAXException {

		charValue = "";
		log.log(Level.FINEST,() ->"CA : StartEle " +qName);
		if (qName.equals("fileHeader")) {
			this.fileFormatVersion = atts.getValue(FILE_FORMAT_VERSION);
			this.vendorName = atts.getValue(VENDOR_NAME);
			this.dnPrefix = atts.getValue(DN_PREFIX);
			log.log(Level.FINEST,() ->"CA : fileFormatVersion " +this.fileFormatVersion);
			log.log(Level.FINEST,() ->"CA : vendorName " +this.vendorName);
			log.log(Level.FINEST,() ->"CA : dnPrefix " +this.dnPrefix);
		} else if (qName.equals("fileSender")) {
			this.fsLocalDN = atts.getValue(LOCAL_DN);
			this.elementType = atts.getValue(ELEMENT_TYPE);
			log.log(Level.FINEST,() ->"CA : fsLocalDN {} " +this.fsLocalDN);
			log.log(Level.FINEST,() ->"CA : elementType " +this.elementType);
		} else if (qName.equals("measCollec")) {
			if (atts.getValue("beginTime") != null) {
				// header
				collectionBeginTime = atts.getValue("beginTime");
			} else if (atts.getValue(END_TIME) != null) {
				// footer
			}
			log.log(Level.FINEST,() ->"CA : collectionBeginTime " +collectionBeginTime);
		} else if (qName.equals("measData")) {
			measNameMap = new HashMap();
			clusterMap = new HashMap();

			origMeasNameMap = new HashMap<String, String>();
			occuranceMap = new HashMap<>();
			if (hashData) {
				loaderClass = new HashMap<String, HashMap<String, HashMap<String, String>>>();
				objectMap = new HashMap<String, String>();
				dataMap = new HashMap<String, HashMap<String, String>>();
				flexMoidMap = new HashMap();
				dynMoidMap = new HashMap<>();
				flexDynMoidMap = new HashMap<>();
			}

		} else if (qName.equals("managedElement")) {
			this.meLocalDN = atts.getValue(LOCAL_DN);
			this.userLabel = atts.getValue(USER_LABEL);
			this.swVersion = atts.getValue(SW_VERSION);
			log.log(Level.FINEST,() ->"CA : meLocalDN " +this.meLocalDN);
			log.log(Level.FINEST,() ->"CA : userLabel " +this.userLabel);
			log.log(Level.FINEST,() ->"CA : swVersion " +this.swVersion);
		} else if (qName.equals("measInfo")) {
			this.measInfoId = atts.getValue(MEAS_INFO_ID);
			log.log(Level.FINEST,() ->"CA : measInfoId " +this.measInfoId);
			if (measInfoPrefixSet.contains(measInfoId)) {
				isPrefixMeasInfo = true;
			}
			log.log(Level.FINEST,() ->"CA : measInfoPrefixSet " +measInfoPrefixSet);
			log.log(Level.FINEST,() ->"CA : isPrefixMeasInfo " +isPrefixMeasInfo);
			

			if (hashData && readVendorIDFrom.equals(MEAS_INFO_ID)) {
				if (measInfoId.contains("=")) {
					getLoaderName(parseFileName(measInfoId, measInfoIdPattern));
				} else {
					getLoaderName(measInfoId);
				}
			}
		} else if (qName.equals("job")) {
			this.jobId = atts.getValue(JOB_ID);
			log.log(Level.FINEST,() ->"CA : jobId " +this.jobId);
		} else if (qName.equals("granPeriod")) {
			granularityPeriodDuration = getSeconds(atts.getValue("duration"));
			granularityPeriodEndTime = atts.getValue(END_TIME);
			log.log(Level.FINEST,() ->"CA : granularityPeriodDuration " +granularityPeriodDuration);
			log.log(Level.FINEST,() ->"CA : granularityPeriodEndTime " +granularityPeriodEndTime);
		} else if (qName.equals("repPeriod")) {
			repPeriodDuration = getSeconds(atts.getValue("duration"));
			log.log(Level.FINEST,() ->"CA : repPeriodDuration " +repPeriodDuration);
		} else if (qName.equals("measTypes")) {
			log.finest(DN);
		} else if (qName.equals("measType")) {
			measIndex = atts.getValue("p");
			log.log(Level.FINEST,() ->"CA : measIndex " +measIndex);
		} else if (qName.equals("measValue")) {
			this.suspectFlag = "";
			this.measObjLdn = atts.getValue(MEAS_OBJ_LDN);
			log.log(Level.FINEST,() ->"CA : measObjLdn " +this.measObjLdn);
			if(readVendorIDFrom.equals("data")||readVendorIDFrom.equals(MEAS_OBJ_LDN))
			{
				String objectClassForAddVendorID=parseFileName(measObjLdn,objectMask);
				log.log(Level.FINEST,() ->"Result of extracting the tag id " +objectClassForAddVendorID);
	            String addVendorIdFromProperty;
	            if(measInfoPrefixSet.contains(objectClassForAddVendorID))
	            {
	            	addVendorIdFromProperty=objectClassForAddVendorID+"_";
	            }
	            else
	            {
	            	addVendorIdFromProperty = "";
	            }
	            log.log(Level.FINEST,() ->"CA : addVendorIdFromProperty " +addVendorIdFromProperty);
	            
	            for(String measIndexFromMap:measTypeMap.keySet())
            	{
            		String charValueAfterMeasObjectLdn=addVendorIdFromProperty+measTypeMap.get(measIndexFromMap);
            		measNameMap.put(measIndexFromMap, extractCounterName(charValueAfterMeasObjectLdn));
    				clusterMap.put(measIndexFromMap, clusterField);
    				origMeasNameMap.put(measIndexFromMap, charValueAfterMeasObjectLdn);
    				String key = extractCounterName(charValueAfterMeasObjectLdn);
    				if(key.contains("_")) {
    					key = key.split("_")[0];
    				}
    				if(occuranceMap.containsKey(key))
    				{
    					occuranceMap.put(key,occuranceMap.get(key)+1);
    				}
    				else
    				{
    					occuranceMap.put(key,1);
    				}
    					
            	}
	            log.log(Level.FINEST,() ->"CA : measTypeMap " +measTypeMap);
	            log.log(Level.FINEST,() ->"CA : measNameMap " +measNameMap);
	            log.log(Level.FINEST,() ->"CA : clusterMap " +clusterMap);
	            log.log(Level.FINEST,() ->"CA : origMeasNameMap " +origMeasNameMap);
	            log.log(Level.FINEST,() ->"CA : occuranceMap {} " +occuranceMap);
	            measTypeMap.clear();
	            log.log(Level.FINEST,() ->"CA : measTypeMap " +measTypeMap);
	   
			}
	           
	            
            
			handleTAGmoid(measObjLdn);
			if (hashData && readVendorIDFrom.equalsIgnoreCase(MEAS_INFO_ID)) {
				counterValueMap = new HashMap<String, String>();
				flexFilterMap = new HashMap();
				dynIndexMap = new HashMap<>();
				flexDynIndexMap = new HashMap<>();
			} else if (hashData
					&& (readVendorIDFrom.equalsIgnoreCase(MEAS_OBJ_LDN) || readVendorIDFrom
							.equalsIgnoreCase("data")|| readVendorIDFrom.equalsIgnoreCase("file"))) {

				counterValueMap = new HashMap<String, String>();
				flexFilterMap = new HashMap();
				dynIndexMap = new HashMap<>();
				flexDynIndexMap = new HashMap<>();
				log.log(Level.FINEST,() -> "objectClass before getting loader name : " +objectClass);
				getLoaderName(objectClass);

			} else {
				try {
					if (sourceFile != null) {
						if ((oldObjClass == null)
								|| !oldObjClass.equals(objectClass)) {
							// close meas file
							if (measFile != null) {
								log.log(Level.FINEST,"R:measFile not null");
								measFile.close();
								measFile=null;
							}
							if (vectorMeasFile != null) {
								log.log(Level.FINEST,
										"R:vectormeasFile not null");
								vectorMeasFile.close();
								vectorMeasFile=null;
								
							}
							if (flexMeasFile != null) {
								log.log(Level.FINEST, "R:flexMeasFile not null");
								flexMeasFile.close();
								flexMeasFile=null;
							}
							if (dynMeasFile != null) {
								log.log(Level.FINEST, "R:dynMeasFile not null");
								dynMeasFile.close();
								dynMeasFile=null;
							}
							if (flexDynMeasFile != null) {
								log.log(Level.FINEST, "R:flexDynMeasFile not null");
								flexDynMeasFile.close();
								flexDynMeasFile=null;
							}
							// create new measurementFile
							DFormat dfmm = dfc.getFormatWithTagID(interfaceName,
									objectClass);
							if(dfmm != null)
							{
								measFile = Main.createMeasurementFile(sourceFile,
										objectClass, techPack, setType, setName,
										workerName, log);
							}
							
							DFormat df = dfc.getFormatWithTagID(interfaceName,
									objectClass + vectorPostfix);

							if (df != null) {
								vectorMeasFile = Main.createMeasurementFile(
										sourceFile,
										objectClass + vectorPostfix, techPack,
										setType, setName, workerName, log);
							}
							if (hasFlexCounters) {
								df = dfc.getFormatWithTagID(interfaceName,
										objectClass + flexPostfix);
								if (createOwnFlexFile && df != null) {
									flexMeasFile = Main.createMeasurementFile(
											sourceFile, objectClass
													+ flexPostfix, techPack,
											setType, setName, workerName, log);
								}
							}
							if (hasDynCounters) {
								df = dfc.getFormatWithTagID(interfaceName,
										objectClass + dynPostfix);
								if (createOwnDynFile && df != null) {
									dynMeasFile = Main.createMeasurementFile(
											sourceFile, objectClass
													+ dynPostfix, techPack,
											setType, setName, workerName, log);
								}
							}
							if (hasFlexDynCounters) {
								df = dfc.getFormatWithTagID(interfaceName,
										objectClass + flexdynPostfix);
								if (createOwnFlexDynFile && df != null) {
									flexDynMeasFile = Main.createMeasurementFile(
											sourceFile, objectClass
													+ flexdynPostfix, techPack,
											setType, setName, workerName, log);
								}
							}
							oldObjClass = objectClass;

						}
					}
				} catch (final Exception e) {
					log.log(Level.FINEST,() -> "ERROR_OPEN_MEAS_DATA" +e);
					e.printStackTrace();
					throw new SAXException(ERROR_OPEN_MEAS_DATA
							+ e.getMessage(), e);
				}
			}
		} else if (qName.equals("measResults")) {
			log.log(Level.FINEST,DN);
		} else if (qName.equals("r")) {
			this.measValueIndex = atts.getValue("p");
			log.log(Level.FINEST,() ->"CA : measValueIndex " +this.measValueIndex);
		} else if (qName.equals("suspect")) {
			log.log(Level.FINEST,DN);
		} else if (qName.equals("fileFooter")) {
			log.log(Level.FINEST, DN);
		}
	}
	
		
	private void getLoaderName(String tagId) {
		String loaderName;
		DataFormatCache cache = DataFormatCache.getCache();
		log.log(Level.FINEST,() -> "DataformatCache : " +cache);
		log.log(Level.FINEST,() -> "get dataformat with interface : " +interfaceName);
		log.log(Level.FINEST,() ->"and tagId : " +tagId);
		DFormat df = cache.getFormatWithTagID(interfaceName, tagId);
		if (df == null) {
			log.log(Level.FINEST,"cannot get dataformat , trying with vectorPostfix");
			df = cache.getFormatWithTagID(interfaceName, tagId + vectorPostfix);
		}
		/*MyCodeStart*/
		if (df == null && hasFlexVectorCounters) {
			log.log(Level.FINEST,"cannot get dataformat , trying with flexVectorPostfix");
			df = cache.getFormatWithTagID(interfaceName, tagId + flexVectorPostfix);
		}
		/*MyCodeEnd*/
		if (df == null && hasFlexDynCounters) {
			log.log(Level.FINEST,"cannot get dataformat , trying with flexdynPostfix");
			df = cache.getFormatWithTagID(interfaceName, tagId + flexdynPostfix);
		}
		if (df == null && hasFlexCounters) {
			log.log(Level.FINEST, "cannot get dataformat , trying with flexPostfix");
			df = cache.getFormatWithTagID(interfaceName, tagId + flexPostfix);
		}
		
		if (df == null && hasDynCounters) {
			log.log(Level.FINEST, "cannot get dataformat , trying with dynPostFix");
			df = cache.getFormatWithTagID(interfaceName, tagId + dynPostfix);
		}
		if (df == null) {
			log.log(Level.FINEST,() -> "dataformat not found for interface : " +interfaceName);
			log.log(Level.FINEST,() ->" and tagId : " +tagId);
			dataFormatNotFound = true;
			loaderName = null;
		} else {
			loaderName = df.getFolderName();
			objectMap.put(tagId, loaderName);
			if (!loaderClass.containsKey(loaderName)) {
				moidMap = new HashMap<>();
				loaderClass.put(loaderName, moidMap);
			} else {
				moidMap = loaderClass.get(loaderName);
			}
		}
		log.log(Level.FINEST,() -> "populated objectMap : " +objectMap);
		log.log(Level.FINEST,() -> "Obtained Loader name = " +loaderName);
	}

	private void handleTAGmoid(String value) {

		this.objectClass = "";
			
		if ("file".equalsIgnoreCase(readVendorIDFrom)) {
			objectClass = parseFileName(sourceFile.getName(), objectMask)+objectClassPostFix;

		} else if ("data".equalsIgnoreCase(readVendorIDFrom)
				|| MEAS_OBJ_LDN.equalsIgnoreCase(readVendorIDFrom)) {

			// if moid is empty and empty moids are filled.
			if (fillEmptyMoid && (value.length() <= 0)) {
				if (fillEmptyMoidStyle.equalsIgnoreCase("static")) {
					value = fillEmptyMoidValue;
				} else {
					value = measValueIndex + "";
				}
			}

			// read vendor id from data
			objectClass = parseFileName(value, objectMask)+objectClassPostFix;

		} else if (MEAS_INFO_ID.equalsIgnoreCase(readVendorIDFrom)) {
			if (measInfoId.contains("=")) {
				objectClass = parseFileName(this.measInfoId, measInfoIdPattern);
			} else {
				objectClass = measInfoId+objectClassPostFix;
			}
		}
		log.log(Level.FINEST,() -> "objectClass " +objectClass);
		flexCounterBin = new HashMap();
		if (hasFlexCounters) {
			final DFormat df = dfc.getFormatWithTagID(interfaceName,
					objectClass + flexPostfix); 
			if (df != null) {
				final Set flexCounters = getDataIDFromProcessInstructions(
						interfaceName, objectClass + flexPostfix,
						flexCounterTag);
				final Set keyColumns = getDataIDFromProcessInstructions(
						interfaceName, objectClass + flexPostfix, keyColumnTag);
				flexCounterBin.put(FILE_NAME, sourceFile.getName());
				flexCounterBin.put(FLEX_COUNTERS, flexCounters);
				flexCounterBin.put(KEY_COLUMNS, keyColumns);
			}
		}
		log.log(Level.FINEST,() ->"CA : flexCounterBin " +flexCounterBin);
		
		
		if (hasDynCounters) {
			final DFormat df = dfc.getFormatWithTagID(interfaceName,
					objectClass + dynPostfix);
			if (df != null) {
				final Set<String> dynCounters = getDataIDFromProcessInstructions(
						interfaceName, objectClass + dynPostfix,
						dynCounterTag);
				final Set<String> keyColumns = getDataIDFromProcessInstructions(
						interfaceName, objectClass + dynPostfix, keyColumnTag);
				dynCounterBin = new HashMap<>();
				dynCounterBin.put(FILE_NAME, sourceFile.getName());
				dynCounterBin.put("dynCounters", dynCounters);
				dynCounterBin.put(KEY_COLUMNS, keyColumns);
			}
		}
		log.log(Level.FINEST,() ->"CA : dynCounterBin " +dynCounterBin);
		
		/*MyCodeStart*/
		Set<String> flexRangeCounters = new HashSet<>();
		Set<String> keyColumns = new HashSet<>();
		Set<String> cmVectorCounters = new HashSet<>();
		Set<String> rangeCounters = new HashSet<>();
		if (hasFlexVectorCounters) {
			final DFormat df = dfc.getFormatWithTagID(interfaceName, objectClass
					+ flexVectorPostfix);
			if (df != null) {
					log.log(Level.FINEST,() -> "T:The ObjectClass: " +objectClass);
		
						/*MyCodeStart*/
				flexRangeCounters = getDataIDFromProcessInstructions(
								interfaceName, objectClass + flexVectorPostfix,
								flexVectorTag);
				
				
				flexCompVectors = getDataIDFromProcessInstructions(
								interfaceName, objectClass + flexVectorPostfix,
								flexCompVectorTag);
		
				keyColumns.addAll(getDataIDFromProcessInstructions(
						interfaceName, objectClass + flexVectorPostfix, keyColumnTag));
				flexRangeCounters.addAll(flexCompVectors);

			}
		}
		flexDynCounterBin = new HashMap<>();
		if (hasFlexDynCounters) {
			final DFormat df = dfc.getFormatWithTagID(interfaceName,
					objectClass + flexdynPostfix); 
			if (df != null) {
				final Set flexDynCounters = getDataIDFromProcessInstructions(
						interfaceName, objectClass + flexdynPostfix,
						flexDynTag);
				final Set keyColumns1 = getDataIDFromProcessInstructions(
						interfaceName, objectClass + flexdynPostfix, keyColumnTag);
				flexDynCounterBin.put(FILE_NAME, sourceFile.getName());
				flexDynCounterBin.put(FLEX_DYN_COUNTERS, flexDynCounters);
				flexDynCounterBin.put(KEY_COLUMNS, keyColumns1);
			}
		}
		log.log(Level.FINEST,() ->"CA : flexDynCounterBin " +flexDynCounterBin);
		final DFormat df = dfc.getFormatWithTagID(interfaceName, objectClass
				+ vectorPostfix);
		if (df != null) {
			
			log.log(Level.FINEST,() -> "T: ObjectClass: " +objectClass);
			// VECTOR counters
			rangeCounters = getDataIDFromProcessInstructions(
					interfaceName, objectClass + vectorPostfix, rangeCounterTag);
			log.log(Level.FINEST,() -> "T:ObjectClass: " +objectClass);
			

			compressedVectorCounters.addAll(getDataIDFromProcessInstructions(
					interfaceName, objectClass + vectorPostfix,
					compressedVectorTag));
			
			log.log(Level.FINEST,() ->"CA : compressedVectorCounters " +compressedVectorCounters);
			rangeCounters.addAll(compressedVectorCounters);
			// we search instructions also from objectClass+vectorPostfix
			
			keyColumns.addAll(getDataIDFromProcessInstructions(
					interfaceName, objectClass + vectorPostfix, keyColumnTag));

			// we search instructions also from objectClass+vectorPostfix

			keyColumns.addAll(getDataIDFromProcessInstructions(interfaceName,
					objectClass + vectorPostfix, keyColumnTag));
		
			// CMVECTOR counters
			cmVectorCounters = getDataIDFromProcessInstructions(
					interfaceName, objectClass + vectorPostfix, cmVectorTag);

			cmVectorCounters.addAll(getDataIDFromProcessInstructions(
					interfaceName, objectClass + vectorPostfix, cmVectorTag));
			
			
		}
		
		final Set<String> finalRangeCounters = rangeCounters;
		final Set<String> finalCmVectorCounters = cmVectorCounters;
		final Set<String> finalFlexRangeCounters = flexRangeCounters;
		final Set<String> finalKeyColumns = keyColumns;
		final Set<String> finalFlexCompVectors = flexCompVectors;
		log.log(Level.FINEST,() ->"CA : cmVectorCounters " +finalCmVectorCounters);
		log.log(Level.FINEST,() -> "CA : FlexVectorCounters " +finalFlexRangeCounters);
		log.log(Level.FINEST,() ->"CA : FlexCompressedVectorCounters " +finalFlexCompVectors);
		log.log(Level.FINEST,() ->"CA : keyColumns " +finalKeyColumns);
		log.log(Level.FINEST,() ->"CA : rangeCounters " +finalRangeCounters);
			// new measurement started
			vectorMeasurement = new HashMap();
			vectorMeasurement.put(FILE_NAME, sourceFile.getName());
			vectorMeasurement.put("rangeCounters", rangeCounters);
			vectorMeasurement.put("cmVectorCounters", cmVectorCounters);
			vectorMeasurement.put(FLEX_RANGE_COUNTERS, flexRangeCounters);
			vectorMeasurement.put(KEY_COLUMNS, keyColumns);
			log.log(Level.FINEST,() ->"CA : vectorMeasurement " +vectorMeasurement);

		}

	private Set getDataIDFromProcessInstructions(final String interfacename,
			final String objectClass, final String key) {
		final Set result = new HashSet();
		try {

			final DFormat df = dfc.getFormatWithTagID(interfacename,
					objectClass);

			final List dItemList = df.getDitems();

			final Iterator iter = dItemList.iterator();
			while (iter.hasNext()) {

				final DItem di = (DItem) iter.next();
				if (di.getProcessInstruction() != null) {
					final StringTokenizer token = new StringTokenizer(
							di.getProcessInstruction(), ",");
					while (token.hasMoreElements()) {
						final String t = (String) token.nextElement();

						if (t.equalsIgnoreCase(key)) {
							result.add(di.getDataID());
						}
					}
				}
			}

			if (result.size() == 0) {
				log.log(Level.FINEST,() -> "ResultSet not updated");
			} else {
				log.log(Level.FINEST,() -> "ResultSet  updated");
			}
		} catch (Exception e) {
			log.warning("Error while retrieving DataIDs from ProcessInstructions");

		}

		return result;

	}

	

	/**
	 * Rips PT and S values off from the value.
	 * 
	 * @param value
	 *            Contains the duration value
	 * @return the duration in seconds
	 */
	private String getSeconds(final String value) {
		String result = null;
		if (value != null){
			if (value.contains("S"))
			{
				result = value.substring(2, value.indexOf('S'));	
			}
			if (value.contains("M"))
			{
				String result_min = value.substring(2,value.indexOf('M'));
				Integer minute = Integer.parseInt(result_min);
				int result_mins = minute * 60;
				result = String.valueOf(result_mins);
			}
		}
		return result;
	}

	private String nameField = "";
	private String clusterField = "";

	private String extractCounterName(final String counterName) {
		final int index1 = counterName.indexOf("#");
		final int index2 = counterName.indexOf(".", index1);
		if (index1 >= 0) {
			if (index2 > index1) { // Format NAME#cluster.NAME -> NAME.NAME and
									// Cluster
				nameField = counterName.substring(0, index1)
						+ counterName.substring(index2, counterName.length());
				clusterField = counterName.substring(index1 + 1, index2);
			} else { // Format NAME#Cluster -> NAME and Cluster
				nameField = counterName.substring(0, index1);
				clusterField = counterName.substring(index1 + 1,
						counterName.length());
			}
		} else { // Format NAME -> NAME
			nameField = counterName;
			clusterField = "";
		}
		return nameField;
	}

	@Override
	public void endElement(final String uri, final String name,
			final String qName) throws SAXException {
		
		log.log(Level.FINEST,() ->"CA : EndEle " +qName);
		if (qName.equals("fileHeader")|| qName.equals("fileSender")
				|| qName.equals("measCollec")|| qName.equals("managedElement")
				|| qName.equals("job") || qName.equals("granPeriod")
				|| qName.equals("repPeriod") || qName.equals("fileFooter")) {
			log.log(Level.FINEST,DN);
		}
		
		/*if (qName.equals("fileHeader")) {
			log.log(Level.FINEST,DN);
		} else if (qName.equals("fileSender")) {
			log.log(Level.FINEST,DN);
		} else if (qName.equals("measCollec")) {
			log.log(Level.FINEST,DN);
		} */else if (qName.equals("measData")) {
			if (hashData) {
				handleTAGMeasData();
			}
		} /*else if (qName.equals("managedElement")) {
			log.log(Level.FINEST,DN);
		} */else if (qName.equals("measInfo")) {
			dataFormatNotFound = false;
			isPrefixMeasInfo = false;
		} /*else if (qName.equals("job")) {
			log.log(Level.FINEST,DN);
		} else if (qName.equals("granPeriod")) {
			log.log(Level.FINEST,DN);
		} else if (qName.equals("repPeriod")) {
			log.finest(DN);
		}*/ else if (qName.equals("measTypes")) {
			measNameMap = strToMap(charValue);
		} else if (qName.equals("measType")) {
			
			if(readVendorIDFrom.equalsIgnoreCase("data")||readVendorIDFrom.equalsIgnoreCase(MEAS_OBJ_LDN))
			{
				measTypeMap.put(measIndex,charValue);
			}
			else
			{
				if (isPrefixMeasInfo) {
					 charValue = measInfoId+"_"+charValue;
				}
					measNameMap.put(measIndex, extractCounterName(charValue));
					clusterMap.put(measIndex, clusterField);
					origMeasNameMap.put(measIndex, charValue);
			}
		} else if (qName.equals("measValue") && !dataFormatNotFound) {
			if (hashData) {
				handleMoidMap(measObjLdn);
			} else {
				/*Commenting this try-catch block try {*/

					if (measFile == null && vectorMeasFile == null
							&& flexMeasFile == null && dynMeasFile == null && flexDynMeasFile == null) {
						log.log(Level.FINEST,"Measurement file null"); 
						log.log(Level.FINEST,() -> "The PERIOD_DURATION: "
								+granularityPeriodDuration);
						log.log(Level.FINEST,() -> "The repPeriodDuration: "
								+repPeriodDuration);
						// DATETIME_ID calculated from end time
						final String begin = calculateBegintime();
						if (begin != null) {
							log.log(Level.FINEST,() -> "The DATETIME_ID: " +begin);
						}
						log.log(Level.FINEST,() -> "The collectionBeginTime: " +collectionBeginTime);
						log.log(Level.FINEST,() -> "DC_SUSPECTFLAG: " +suspectFlag);
						String dummyFile = (sourceFile == null ? DUMMY_FILE 
								: sourceFile.getName());
						log.log(Level.FINEST,() -> "The filename: " +dummyFile);
						log.log(Level.FINEST,() -> "The JVM_TIMEZONE: " + JVM_TIMEZONE);
						String dummyDir = (sourceFile == null ? DUMMY_DIR
								: sourceFile.getDir());
						log.log(Level.FINEST,() ->"The DIRNAME: " +dummyDir);
						log.log(Level.FINEST,() -> "The measInfoId: " +measInfoId);
						log.log(Level.FINEST,() -> "MOID: " +measObjLdn);
						log.log(Level.FINEST,() -> "The objectClass: " +objectClass);
						log.log(Level.FINEST,() -> "The vendorName: " +vendorName);
						log.log(Level.FINEST,() -> "The fileFormatVersion: "
								+fileFormatVersion);
						log.log(Level.FINEST,() -> "The dnPrefix: " +dnPrefix);
						log.log(Level.FINEST,() -> "The localDn: " +fsLocalDN);
						log.log(Level.FINEST,() -> "The managedElementLocalDn: "
								+meLocalDN);
						log.log(Level.FINEST,() -> "The elementType: " +elementType);
						log.log(Level.FINEST,() -> "The userLabel: " +userLabel);
						log.log(Level.FINEST,() -> "The swVersion: " +swVersion);
						// collectionEndTime received so late, that migth not be
						// used
						log.log(Level.FINEST,() -> "The endTime: "
								 +granularityPeriodEndTime);
						log.log(Level.FINEST,() -> "The jobId: " +jobId);
					} else {
						/*if(measFile != null)
						{
							measFile.addData(DC_SUSPECT_FLAG, suspectFlag);
							measFile.addData("MOID", measObjLdn);
							measFile.addData(addValues());
							measFile.saveData();
						}

						if (createOwnFlexFile && hasFlexCounters
								&& flexMeasFile != null) {
							flexMeasFile.addData(DC_SUSPECT_FLAG, suspectFlag);
							flexMeasFile.addData("MOID", measObjLdn);
							flexMeasFile.addData(addValues());
							flexMeasFile.saveData();
						}
						
						if (createOwnDynFile && hasDynCounters
								&& dynMeasFile != null) {
							dynMeasFile.addData(DC_SUSPECT_FLAG, suspectFlag);
							dynMeasFile.addData("MOID", measObjLdn);
							dynMeasFile.addData(addValues());
							dynMeasFile.saveData();
						}*/
						if(measFile != null)
							addSaveMeasFile(measFile);
						if(createOwnFlexFile && hasFlexCounters
								&& flexMeasFile != null)
							addSaveMeasFile(flexMeasFile);

						if(createOwnDynFile && hasDynCounters
								&& dynMeasFile != null)
							addSaveMeasFile(dynMeasFile);
						if(createOwnFlexDynFile && hasFlexDynCounters
								&& flexDynMeasFile != null)
							addSaveMeasFile(flexDynMeasFile);
						if (vectorMeasFile != null) {
							int maxBinSize = 0;
							log.log(Level.FINEST,() -> "E:StoreMap "
									+vectorBinValueMap);
							for (String counterName : vectorBinValueMap
									.keySet()) {
								int binSize = vectorBinValueMap
										.get(counterName).size();
								if (binSize > maxBinSize) {
									maxBinSize = binSize;
								}

							}
							final String begin1 = calculateBegintime();
							String checkZero = "NIL";
							for (int i = 0; i < maxBinSize; i++) {
								String binIndex = i + "";
								int flag = 0;
								Map<String, String> value;
								for (String counterName : vectorBinValueMap
										.keySet()) {
									value = vectorBinValueMap.get(counterName);

									if (value.containsKey(binIndex)
											&& binIndex != null) {
										if (flag == 0) {
											flag = 1;
										}
										if (checkZero.equals(value
												.get(binIndex))) {
											vectorMeasFile.addData(counterName,
													null);
										} else {
											vectorMeasFile.addData(counterName,
													value.get(binIndex));
										}
										
										log.log(Level.FINEST,() ->" DCVECTOR_INDEX " +binIndex);
										vectorMeasFile.addData(
												DC_VECTOR_INDEX, binIndex);
										vectorMeasFile.addData(counterName
												+ rangePostfix, binIndex);
										vectorMeasFile.addData("MOID",
												measObjLdn);
										vectorMeasFile.addData(
												DC_SUSPECT_FLAG, suspectFlag);
										vectorMeasFile.addData(addValues());
									}

								}
								if(flag == 1)
									saveMeasFile(vectorMeasFile);
								/*try {
									if (flag == 1)
										vectorMeasFile.saveData();
								} catch (final Exception e) {
									log.log(Level.WARNING,
											ERROR_SAVE_MEAS_DATA, e);
									e.printStackTrace();
									throw new SAXException(
											ERROR_SAVE_MEAS_DATA
													+ e.getMessage(), e);

								}*/
							}

							Map<String, String> value;
							for (String counterName : vectorBinValueMap
									.keySet()) {
								value = vectorBinValueMap.get(counterName);
								Iterator iterator = value.keySet().iterator();
								while (iterator.hasNext()) {
									String i1 = (String) iterator.next();
									if ((Integer.parseInt(i1) > maxBinSize)&& (value.containsKey(i1) && i1 != null
												&& value.get(i1) != null)) {
											if (checkZero.equals(value.get(i1))) {
												vectorMeasFile.addData(
														counterName, null);
											} else {
												vectorMeasFile.addData(
														counterName,
														value.get(i1));
											}
											
											vectorMeasFile.addData(
													DC_VECTOR_INDEX, i1);
											vectorMeasFile.addData(counterName
													+ rangePostfix, i1);
											vectorMeasFile.addData("MOID",
													measObjLdn);
											vectorMeasFile.addData(
													DC_SUSPECT_FLAG,
													suspectFlag);
											vectorMeasFile.addData(addValues());
											saveMeasFile(vectorMeasFile);
											/*try {
												vectorMeasFile.saveData();
											} catch (final Exception e) {
												log.log(Level.WARNING,
														ERROR_SAVE_MEAS_DATA,
														e);
												e.printStackTrace();
												throw new SAXException(
														ERROR_SAVE_MEAS_DATA
																+ e.getMessage(),
														e);

											}*/
										}
								}

							}

						}
						vectorBinValueMap.clear();
					}

				/*} catch (final Exception e) {
					log.log(Level.FINEST,() -> "ERROR_SAVE_MEAS_DATA" +e);
					e.printStackTrace();
					throw new SAXException(ERROR_SAVE_MEAS_DATA
							+ e.getMessage(), e);
				}*/
			}
		} else if (qName.equals("measResults")) {
			final Map measValues = strToMap(charValue);
			if (measValues.keySet().size() == measNameMap.keySet().size()) {
				final Iterator it = measValues.keySet().iterator();
				while (it.hasNext()) {
					final String s = (String) it.next();
					String origValue = (String) measValues.get(s);
					if ((origValue != null)
							&& (origValue.equalsIgnoreCase("NIL") || origValue
									.trim().equalsIgnoreCase("") || origValue.equalsIgnoreCase("NULL"))) {
						origValue = "";
						log.finest("Setting the value to null as in-valid data being received from the Node");
					}
					if (hashData) {
						boolean isFlexVal = false;
						boolean isDynVal = false;
						if (hasFlexCounters && flexCounterBin != null) {
							log.log(Level.FINEST,() -> "Check flex counter Bin " +flexCounterBin);
							if(!flexCounterBin.isEmpty()) {
								isFlexVal = checkIfFlex((String) measNameMap.get(s));
							}
						}
						if (hasDynCounters && dynCounterBin != null) {
							log.log(Level.FINEST,() -> "Check dyn counter Bin {} " +dynCounterBin);
							if(!dynCounterBin.isEmpty()) {
								isDynVal = checkIfDyn((String) measNameMap.get(s));
							}
						}
						if (!isFlexVal && !isDynVal) {
							counterValueMap.put((String) measNameMap.get(s),
									origValue);
							String logString = (String) measNameMap.get(measValueIndex)
									+ ": " + origValue;
							log.log(Level.FINEST,logString);
						}
					} else {
						if (measFile == null) {
							String logString = (String) measNameMap.get(s)
									+ ": " + origValue;
							log.log(Level.FINEST,logString);
						} else {
							measFile.addData((String) measNameMap.get(s),
									origValue);
							String logString = (String) measNameMap.get(s)
									+ ": " + origValue;
							log.log(Level.FINEST, logString);
						}
					}
				}
			} else {
				log.warning(DATA_MORE_R_THAN_MT);
			}
		} else if (qName.equals("r") && !dataFormatNotFound) {
			if (hashData) {
				if (measNameMap.get(measValueIndex) != null) {
					String origValue = charValue;
					boolean isFlexCounter = false;
					/*MyCodeStart*/
					boolean isFlexVectorCounter = false;
					/*MyCodeEnd*/
					boolean isFlexDynCounter = false;
					if (hasFlexDynCounters && flexDynCounterBin != null) {
						log.log(Level.FINEST,() -> "Check flexdyn counter Bin" +flexDynCounterBin);
						if(!flexDynCounterBin.isEmpty()) {
							isFlexDynCounter = checkIfFlexDyn((String) measNameMap
										.get(measValueIndex));
						}
					}
					boolean isDynCounter = false;
					if (hasFlexCounters && flexCounterBin != null) {
						log.log(Level.FINEST,() -> "Check flex counter Bin" +flexCounterBin);
						if(!flexCounterBin.isEmpty()) {
							isFlexCounter = checkIfFlex((String) measNameMap
									.get(measValueIndex));
						}				
					}
					if (hasDynCounters && dynCounterBin != null) {
						log.log(Level.FINEST,() -> "Check dyn counter Bin" +dynCounterBin);
						if(!dynCounterBin.isEmpty()) {
								isDynCounter = checkIfDyn((String) measNameMap
										.get(measValueIndex));
						}
					}
					/*MyCodeStart*/
					if (hasFlexVectorCounters && vectorMeasurement != null) {
						isFlexVectorCounter = checkIfFlexVector((String) measNameMap
								.get(measValueIndex));
					}
					log.log(Level.FINEST,
						(String) measNameMap.get(measValueIndex));
					String logString = (String) measNameMap.get(measValueIndex)
							+ ": " + origValue;
					log.log(Level.FINEST,logString);
					logString = origMeasNameMap.get(measValueIndex) + ": "
							+ origValue;
					log.log(Level.FINEST,logString);
					/*MyCodeEnd*/
					if ((origValue != null)) {
						Pattern whitespace = Pattern.compile("\\s+");
						Matcher matcher = whitespace.matcher(origValue);
						origValue = matcher.replaceAll("");
					}
					if ((origValue != null)
							&& (origValue.equalsIgnoreCase("NIL") || origValue
									.trim().equalsIgnoreCase("") || origValue.equalsIgnoreCase("NULL"))) {
						origValue = "";
						log.finest("Hashdata: Setting the value to null as in-valid data being received from the Node");
					}

					if (origValue != null) {
						if (origValue.contains(",")) {
							final String counterName = origMeasNameMap
									.get(measValueIndex);
							/*MyCodeStart*/
							
							if(isFlexVectorCounter)
							{
								try {
									String flexCounter = "";
									String flexFilter = "";
									if (containsFilter) {
										Matcher m = getMatcher(counterName, FLEX_COUNTER_PATTERN);
										if (m.matches()) {
											if (m.groupCount() > 1) {
												flexCounter = m.group(1);
												flexFilter = m.group(2);
											} else {
												flexCounter = counterName;
											}
										}
									} else {
										flexCounter = counterName;
									}
									
								Map<String, String> tmpMap = handleVectorCounters(
										vectorMeasurement, flexCounter, flexFilter, origValue);
								if(occuranceMap.get(flexCounter)==1)
								{
									vectorBinValueMap.put(flexCounter, tmpMap);
								}
								else
								{
									if(vectorBinValueMap.containsKey(flexCounter)) {
										tmpMap =  Stream.of(vectorBinValueMap.get(flexCounter), tmpMap)
												  .flatMap(map -> map.entrySet().stream())
												  .collect(Collectors.toMap(
												    Map.Entry::getKey,
												    Map.Entry::getValue,
												    (value1, value2) -> value1));
										vectorBinValueMap.put(flexCounter, tmpMap);
									}
									else
									{
										vectorBinValueMap.put(flexCounter, tmpMap);
									}
								}
								} catch (Exception e) {
									log.info("Exception occured trace below" + e.getMessage());
									e.getStackTrace();

								}
							}
							else {
								String flexFilter = "";
								Map<String, String> tmpMap = handleVectorCounters(
										vectorMeasurement,  counterName,flexFilter,
										origValue);
								// added below empty and null checks as a part of EQEV-107714 fix.
								if(!occuranceMap.isEmpty() && occuranceMap.containsKey(counterName) && occuranceMap.get(counterName)==1)
								{
									vectorBinValueMap.put(counterName, tmpMap);
								}
								else
								{
									if(vectorBinValueMap.containsKey(counterName)) {
										tmpMap =  Stream.of(vectorBinValueMap.get(counterName), tmpMap)
												  .flatMap(map -> map.entrySet().stream())
												  .collect(Collectors.toMap(
												    Map.Entry::getKey,
												    Map.Entry::getValue,
												    (value1, value2) -> value1));
										vectorBinValueMap.put(counterName, tmpMap);
									}
									else
									{
										vectorBinValueMap.put(counterName, tmpMap);
									}
								}
								
							}		
							/*MyCodeEnd*/
						} else if (!isFlexCounter && !isDynCounter && !isFlexVectorCounter) {
							counterValueMap.put(
									(String) measNameMap.get(measValueIndex),
									origValue);
							counterValueMap.put("clusterId",
									(String) clusterMap.get(measValueIndex));

							counterValueMap.put(
									origMeasNameMap.get(measValueIndex),
									origValue);
						}
					}
				} else {
					log.warning(DATA_MORE_R_THAN_MT);
				}
			} else {
				if (measNameMap.get(measValueIndex) != null) {
					boolean isFlexCounter = false;
					if (hasFlexCounters && flexCounterBin != null) {
						isFlexCounter = checkIfFlex((String) measNameMap
								.get(measValueIndex));
					}				
					boolean isDynCounter = false;
					if (hasDynCounters && dynCounterBin != null) {
						isDynCounter = checkIfDyn((String) measNameMap
								.get(measValueIndex));
					}
					boolean isFlexDynCounter = false;
					if (hasFlexDynCounters && flexDynCounterBin != null) {
						isFlexDynCounter = checkIfFlexDyn((String) measNameMap
								.get(measValueIndex));
					}
					String origValue = charValue;
					if ((origValue != null)) {
						origValue = origValue.trim();
					}
					if ((origValue != null)
							&& (origValue.equalsIgnoreCase("NIL") || origValue
									.trim().equalsIgnoreCase("") || origValue.equalsIgnoreCase("NULL"))) {
						origValue = "";
						log.finest(" else block : Setting the value to null as in-valid data being received from the Node");
					}

					if (measFile == null && vectorMeasFile == null&&flexMeasFile == null &&dynMeasFile==null && flexDynMeasFile==null) {
						String logString = (String) measNameMap
								.get(measValueIndex)
								+ ": "
								+ origValue
								+ " clusterId: "
								+ (String) clusterMap.get(measValueIndex);
						log.log(Level.FINEST,logString);

					} else if (origValue != null) {

						String vector = origValue;
						if (vector.contains(",")) {
							final String counterName = origMeasNameMap
									.get(measValueIndex);
							String flexFilter = "";
							Map<String, String> tmpMap = handleVectorCounters(
									vectorMeasurement, counterName,flexFilter,
									vector);
							vectorBinValueMap.put(counterName, tmpMap);

						} else if (hasFlexCounters && isFlexCounter && !flexFilterMap.isEmpty()) {
							final Iterator flexIter = flexFilterMap.keySet().iterator();
							while (flexIter.hasNext()) {
								flexMeasFile.addData((Map) flexFilterMap.get(flexIter.next()));
							}
						} else if (hasDynCounters && isDynCounter && !dynIndexMap.isEmpty()) {
							final Iterator<String> dynIter = dynIndexMap.keySet().iterator();
							while (dynIter.hasNext()) {
								dynMeasFile.addData(dynIndexMap.get(dynIter.next()));
							}
						} 
						else if (hasFlexDynCounters && isFlexDynCounter && !flexDynIndexMap.isEmpty()) {
							final Iterator<String> flexDynIter = flexDynIndexMap.keySet().iterator();
							while (flexDynIter.hasNext()) {
								flexDynMeasFile.addData(flexDynIndexMap.get(flexDynIter.next()));
							}
						} 
						
						else {
							measFile.addData(
									(String) measNameMap.get(measValueIndex),
									origValue);
							measFile.addData("clusterId",
									(String) clusterMap.get(measValueIndex));
							measFile.addData(
									origMeasNameMap.get(measValueIndex),
									origValue);

						}

					}
				} else {
					log.warning(DATA_MORE_R_THAN_MT);
				}
			}

		} else if (qName.equals("suspect")) {
			this.suspectFlag = charValue;
		}/*else if (qName.equals("fileFooter")) {
			log.log(Level.FINEST,() ->DN);
		}*/
	}

	
	private void handleMoidMap(String moid) {

		log.log(Level.FINEST,() -> "Loader Name: " + moidMap.size());
		moid = moid.toUpperCase();

		if (moidMap.containsKey(moid)) {
			counterMap = moidMap.get(moid);
			counterMap.putAll(counterValueMap);
			moidMap.put(moid, counterMap);
		} else {
			moidMap.put(moid, counterValueMap);
		}
		log.log(Level.FINEST,() -> "moidMap: " +moidMap);
		if (hasFlexCounters && !flexFilterMap.isEmpty()) {
			if(flexMoidMap.containsKey(moid)){
				HashMap FlexMap = (HashMap) flexMoidMap.get(moid);
				for(Entry<String, Map> entry : flexFilterMap.entrySet()){
					String key = (String) entry.getKey();
					HashMap<String,Object> value = (HashMap<String, Object>) entry.getValue();
					if(FlexMap.containsKey(key)){
						HashMap<String,Object> newFlexMap =  (HashMap<String, Object>) FlexMap.get(key);
						newFlexMap.putAll(value);
						FlexMap.put(key, newFlexMap);
					}else{
						FlexMap.put(key, value);
					}
				}      			
				flexMoidMap.put(moid,FlexMap);
			}
			else{
				flexMoidMap.put(moid,flexFilterMap);
			}
			log.log(Level.FINEST,() -> "flexMoidMap: " +flexMoidMap);
		}
		else{
			log.finest("flexfiltermap is empty");
		}
		log.log(Level.FINEST,() -> "dynIndexMap: " +dynIndexMap);
		log.log(Level.FINEST,() -> "flexDynIndexMap: " +flexDynIndexMap);
		log.log(Level.FINEST,() -> "dynMoidMap: " +dynMoidMap);
		log.log(Level.FINEST,() -> "flexDynMoidMap: " +flexDynMoidMap);
		if (hasFlexDynCounters && !flexDynIndexMap.isEmpty()) {
			flexDynMoidMap.put(moid, flexDynIndexMap);
		}
		log.log(Level.FINEST,() -> "flexDynMoidMap after: " +flexDynMoidMap);
		if (hasDynCounters && !dynIndexMap.isEmpty()) {
			dynMoidMap.put(moid, dynIndexMap);
		}
		log.log(Level.FINEST,() -> "dynMoidMap: " +dynMoidMap);
		final HashMap<String, String> rowMap = new HashMap<String, String>();

		Iterator iter = moidMap.entrySet().iterator();
		if (iter == null) {
			log.log(Level.FINEST,() -> "Loader Name:is null");
		}

		rowMap.put("PERIOD_DURATION", granularityPeriodDuration);
		log.log(Level.FINEST,() -> " PERIOD_DURATION: " +granularityPeriodDuration);
		rowMap.put("repPeriodDuration", repPeriodDuration);
		log.log(Level.FINEST,() -> " repPeriodDuration: " +repPeriodDuration);
		// DATETIME_ID calculated from end time
		final String begin = calculateBegintime();
		if (begin != null) {
			rowMap.put("DATETIME_ID", begin);
			log.log(Level.FINEST,() -> " DATETIME_ID: " +begin);
		}
		rowMap.put("collectionBeginTime", collectionBeginTime);
		log.log(Level.FINEST,() -> " collectionBeginTime: " +collectionBeginTime);
		rowMap.put(DC_SUSPECT_FLAG, suspectFlag);
		log.log(Level.FINEST,() -> "DC_SUSPECTFLAG: " +suspectFlag);
		rowMap.put(FILE_NAME,
				(sourceFile == null ? DUMMY_FILE : sourceFile.getName()));
		String dummyFile = (sourceFile == null ? DUMMY_FILE : sourceFile.getName());
		log.log(Level.FINEST,() -> " filename: " +dummyFile );
		rowMap.put(JVM_TIME_ZONE, JVM_TIMEZONE);
		log.log(Level.FINEST,() -> " JVM_TIMEZONE: " + JVM_TIMEZONE);
		rowMap.put(DIR_NAME,
				(sourceFile == null ? DUMMY_DIR : sourceFile.getDir()));
		String dummyDir = (sourceFile == null ? DUMMY_DIR : sourceFile.getDir());
		log.log(Level.FINEST,() -> " DIRNAME: "+dummyDir);
		rowMap.put(MEAS_INFO_ID, measInfoId);
		log.log(Level.FINEST,() -> " measInfoId: " +measInfoId);
		rowMap.put("MOID", measObjLdn);
		log.log(Level.FINEST,() -> "MOID: " +measObjLdn);
		rowMap.put("objectClass", objectClass);
		log.log(Level.FINEST,() -> " objectClass: " +objectClass);
		rowMap.put(VENDOR_NAME, vendorName);
		log.log(Level.FINEST,() -> " vendorName: "  +vendorName);
		rowMap.put(FILE_FORMAT_VERSION, fileFormatVersion);
		log.log(Level.FINEST,() -> "fileFormatVersion: " +fileFormatVersion);
		rowMap.put(DN_PREFIX, dnPrefix);
		log.log(Level.FINEST,() -> " dnPrefix: " +dnPrefix);
		rowMap.put(LOCAL_DN, fsLocalDN);
		log.log(Level.FINEST,() -> " localDn: " +fsLocalDN);
		rowMap.put("managedElementLocalDn", meLocalDN);
		log.log(Level.FINEST,() -> " managedElementLocalDn: " +meLocalDN);
		rowMap.put(ELEMENT_TYPE, elementType);
		log.log(Level.FINEST,() -> " elementType: " +elementType);
		rowMap.put(USER_LABEL, userLabel);
		log.log(Level.FINEST,() -> " userLabel: " +userLabel);
		rowMap.put(SW_VERSION, swVersion);
		log.log(Level.FINEST,() -> " swVersion: " +swVersion);
		// collectionEndTime received so late, that migth not be used
		rowMap.put(END_TIME, granularityPeriodEndTime);
		log.log(Level.FINEST,() -> " endTime: " +granularityPeriodEndTime);
		rowMap.put(JOB_ID, jobId);
		log.log(Level.FINEST,() -> " jobId: " +jobId);
		log.log(Level.FINEST,() -> "objectmap before accessing : " +objectMap);
		final String loadname = objectMap.get(objectClass);
		 
		log.log(Level.FINEST,() ->" loaderName : " +loadname);
		dataMap.put(moid + loadname, rowMap);
		log.log(Level.FINEST,() -> "rowMap: " +rowMap);
		log.log(Level.FINEST,() -> "dataMap: " +dataMap);
		Map<String, Map<String, String>> vectorBinValueMap1 = new HashMap<>();
		/*for (String counterName1 : vectorBinValueMap.keySet()) {
			vectorBinValueMap1.put(counterName1,
					vectorBinValueMap.get(counterName1));
		}*/
		vectorBinValueMap1.putAll(vectorBinValueMap); 
		vectorBinValueMap.clear();
		log.log(Level.FINEST,() -> "vectorBinValueMap: " +vectorBinValueMap);
		log.log(Level.FINEST,() -> "vectorBinValueMap1: " +vectorBinValueMap1);
		if (vectorData.containsKey(measObjLdn)) {

			Map<String, Map<String, String>> vectorBinValueMap2;
			vectorBinValueMap2 = vectorData.get(measObjLdn);
			vectorBinValueMap1.putAll(vectorBinValueMap2);
			vectorBinValueMap2.clear();
		}
		vectorData.put(measObjLdn, vectorBinValueMap1);
		log.log(Level.FINEST,() -> "vectorData: " +vectorData);
		measInfoAndLdn.put(objectClass + "-" + measObjLdn, objectClass);

	}

	private void handleTAGMeasData() throws SAXException {
		log.log(Level.FINEST,() -> "loaderClass: " +loaderClass);
		log.log(Level.FINEST,() -> "objectMap: " +objectMap);
		if (!loaderClass.isEmpty() && !objectMap.isEmpty()) {
			final HashMap<String, ArrayList<String>> objectMapClone = new HashMap<String, ArrayList<String>>();
			final Iterator<String> objectIter = objectMap.keySet().iterator();
			while (objectIter.hasNext()) {
				final String tagID = objectIter.next();
				final String loader = objectMap.get(tagID);
				log.log(Level.FINEST,() -> "Loader:" +loader);
				if(!objectMapClone.containsKey(loader)){
					ArrayList<String> objectMapClonelist=new ArrayList<String>();
					objectMapClone.put(loader,objectMapClonelist);
				}
				objectMapClone.get(loader).add(tagID);
			}
			final Iterator<String> loadIter = loaderClass.keySet().iterator();
			log.log(Level.FINEST,() -> "loader class keys : " +loaderClass.keySet());
			while (loadIter.hasNext()) {
				final String loaderName = loadIter.next();
				final HashMap<String, HashMap<String, String>> moid = loaderClass
						.get(loaderName);
				boolean flexFlag=false;
				boolean dynFlag=false;
				boolean flexDynFlag = false;
				boolean mainFlag=false;
				boolean useFlexVecPostFix = false;
				
				ArrayList<String> objectClassCloneList = objectMapClone.get(loaderName);
				for(String 	objectClassClone : 	objectClassCloneList)
				{
					useFlexVecPostFix = false;
					log.log(Level.FINEST,() -> "objectClassClone : " +objectClassClone);
					DFormat df = dfc.getFormatWithTagID(interfaceName,
										objectClassClone + vectorPostfix);
					if(df==null) {
						log.log(Level.FINEST,() -> "DF : is null");
						useFlexVecPostFix = true;
						df = dfc.getFormatWithTagID(interfaceName,
								objectClassClone + flexVectorPostfix);
					}
				if (df != null) {
					log.log(Level.FINEST,() ->
							"Creating MeasureMentFile for TagID "
							+(objectClassClone + vectorPostfix));
					log.log(Level.FINEST,() ->
							"Creating MeasureMentFile for Interface Name "
							+interfaceName);
					String tagID = "";
					if(useFlexVecPostFix)
					{
						tagID = objectClassClone + flexVectorPostfix;
					}
					else {
						tagID = objectClassClone + vectorPostfix;
							
					}
					vectorMeasFile = openMeasFile(tagID);
					/*try {
						log.log(Level.FINEST,() ->
								"Creating MeasureMentFile for TagID "
										+(objectClassClone + vectorPostfix));
						log.log(Level.FINEST,() ->
								"Creating MeasureMentFile for Interface Name "
										+interfaceName);
						if(useFlexVecPostFix)
						{
							vectorMeasFile = Main.createMeasurementFile(sourceFile,
									objectClassClone + flexVectorPostfix, techPack,
									setType, setName, workerName, log);
						}
						else {
							vectorMeasFile = Main.createMeasurementFile(sourceFile,
									objectClassClone + vectorPostfix, techPack,
									setType, setName, workerName, log);
						}
					} catch (final Exception e) {
						log.log(Level.FINEST,() ->
								"Error opening vector measurement data" +e);
						e.printStackTrace();
						throw new SAXException(
								"Error opening vector measurement data: "
										+ e.getMessage(), e);
					}*/
					for (String counterName1 : vectorData.keySet()) {
						if(objectClassClone.equals(measInfoAndLdn.get(objectClassClone + "-" + counterName1))){
							log.log(Level.FINEST,"is a vector MO");
							log.log(Level.FINEST,() -> "counterName1 " +counterName1);
							Map<String, Map<String, String>> vectorBinValueMapForEachCounter;
							int maxBinSize = 0;
							vectorBinValueMapForEachCounter = vectorData.get(counterName1);
							maxBinSize = calculateMaxBinSize(vectorBinValueMapForEachCounter);
							/*
							for (Map<String, String> valueMap : vectorBinValueMapForEachCounter.values()) {
							    int binSize = valueMap.size();
							    // ...
							    if (binSize > maxBinSize) {
									maxBinSize = binSize;
								}
							 }*/
							
							int maxval = 0;
							HashSet<String> filterNames = new HashSet<>();
							filterNames.add("");
							maxval = calculateMaxValue(vectorBinValueMapForEachCounter);
							filterNames.addAll(extractFilterNames(vectorBinValueMapForEachCounter));
							/*
							for (Map<String, String> valueMap : vectorBinValueMapForEachCounter.values()) {
								for (String i2 : valueMap.keySet()) {
									if (i2 != null || !i2.equals("0")) {
										if(!i2.contains(":"))
										{
										int binval = Integer.parseInt(i2);
											if (binval > maxval) {
												maxval = binval;
											}
										}
										else {
											int binval = Integer.parseInt(i2.split(":")[0]);
											if (binval > maxval) {
												maxval = binval;
											}
										}
									}
								}
								for (String i2 : valueMap.values()) {
									if(i2.contains(":")&&(i2.indexOf(':')!=i2.length()-1))
										{
											filterNames.add(i2.split(":")[1]);	
										}
								}
							}*/
							
							
							log.log(Level.FINEST,() -> "FilterNames " +filterNames);
							final String begin1 = calculateBegintime();
							String checkZero = "NIL";
							for (int i = 0; i <= maxval; i++) {
								for (String filter : filterNames) {
									String binIndex = i + "";
									String flexBinIndex = i + ":" + filter;
									int flag = 0;
									Map<String, String> value;
									for (Entry<String, Map<String, String>> entry : vectorBinValueMapForEachCounter.entrySet()) {
										String counterName = entry.getKey();
										value =  entry.getValue();
										if (value.containsKey(binIndex)
												&& binIndex != null && filter.equals("")) {
											if (flag == 0) {
												flag = 1;
											}
											if (checkZero.equals(value.get(binIndex))) {
												vectorMeasFile.addData(counterName, null);
											} else {
												vectorMeasFile.addData(counterName, value.get(binIndex));
											}
											
											log.log(Level.FINEST,() ->"DCVECTOR_INDEX " +binIndex);
											vectorMeasFile.addData(
													DC_VECTOR_INDEX, binIndex);
											vectorMeasFile.addData(
													FLEX_FILTERNAME, filter);
											
											vectorMeasFile.addData(
													FILTER_HASH, filter.hashCode()+"");
											/*vectorMeasFile.addData(counterName
													+ rangePostfix, flexBinIndex.split(":")[0]);*/
											vectorMeasFile.addData(counterName
													+ rangePostfix, binIndex);
											vectorMeasFile.addData("MOID",
													counterName1);
											vectorMeasFile.addData(
													DC_SUSPECT_FLAG, suspectFlag);

											vectorMeasFile.addData(addValues());
											
	
										}
										else if(value.containsKey(flexBinIndex)
												&& flexBinIndex != null) {
											log.finest("FlexVector");
											
											String subVal="";
											if (flag == 0) {
												flag = 1;
											}
											if (checkZero.equals(value.get(flexBinIndex))) {
												vectorMeasFile.addData(counterName, null);
											} else {
												subVal=value.get(flexBinIndex);
												if (subVal.charAt(0)== ':') {
													 vectorMeasFile.addData(counterName,"");
												}
												else {
													if(value.get(flexBinIndex).split(":")[0].equals("null")) {
														vectorMeasFile.addData(counterName, "");
													}
													else {
														vectorMeasFile.addData(counterName, value.get(flexBinIndex).split(":")[0]);
													} 
												}
											}
											
						
											log.log(Level.FINEST,() ->
													"Q:CounterName " +counterName);
											
											log.log(Level.FINEST,() -> "DCVECTOR_INDEX " +flexBinIndex.split(":")[0]);
											
											
											if(subVal.indexOf(':')!=subVal.length()-1)
											{
												filter = value.get(flexBinIndex).split(":")[1];												
											}
											
											vectorMeasFile.addData(
													DC_VECTOR_INDEX, flexBinIndex.split(":")[0]);
											
											vectorMeasFile.addData(
													FLEX_FILTERNAME, filter);
											
											vectorMeasFile.addData(
													FILTER_HASH, filter.hashCode()+"");
											vectorMeasFile.addData(counterName
													+ rangePostfix, flexBinIndex.split(":")[0]);
											vectorMeasFile.addData("MOID",
													counterName1);
											vectorMeasFile.addData(
													DC_SUSPECT_FLAG, suspectFlag);
	
											vectorMeasFile.addData(addValues());
											
										}
										else {
											log.log(Level.FINEST,DN);
										}
	
									}
									if(flag == 1)
										saveMeasFile(vectorMeasFile);
									/*
									try {
										if (flag == 1)
											vectorMeasFile.saveData();
									} catch (final Exception e) {
										log.log(Level.WARNING,
												ERROR_SAVE_MEAS_DATA, e);
										e.printStackTrace();
										throw new SAXException(
												ERROR_SAVE_MEAS_DATA
														+ e.getMessage(), e);
	
									}*/
								}
							}
						}

					}

					closeMeasFile(vectorMeasFile);
					/*
					if ((vectorMeasFile != null) && vectorMeasFile.isOpen()) {
						try {
							vectorMeasFile.close();
						} catch (final Exception e) {
							log.log(Level.WARNING,
									ERROR_CLOSE_MEAS_FILE, e);
							e.printStackTrace();
							throw new SAXException(
									ERROR_CLOSE_MEAS_FILE
											+ e.getMessage(), e);
						}
					}*/
				}
				if (hasFlexCounters) {
					DFormat dff = dfc.getFormatWithTagID(interfaceName,
							objectClassClone + flexPostfix);
					if ((dff != null && !flexFlag) && (createOwnFlexFile && flexMoidMap != null)){
							flexFlag=true;
							
							log.log(Level.FINEST,() ->
									"Creating FLEX MeasureMentFile for TagID "
											+(objectClassClone + flexPostfix));
							log.log(Level.FINEST,() ->"Interface Name " +interfaceName);
							flexMeasFile = openMeasFile(objectClassClone
									+ flexPostfix); 
							/*try {
								flexMeasFile = Main.createMeasurementFile(
										sourceFile, objectClassClone
												+ flexPostfix, techPack,
										setType, setName, workerName, log);
							} catch (final Exception e) {
								log.log(Level.WARNING,
										ERROR_OPEN_MEAS_DATA, e);
								e.printStackTrace();
								throw new SAXException(
										ERROR_OPEN_MEAS_DATA
												+ e.getMessage(), e);
							}*/

							final Iterator<String> moidIter = moid.keySet()
									.iterator();
							while (moidIter.hasNext()) {
								final String moidName = moidIter.next();
								final HashMap filterMap = (HashMap) flexMoidMap.get(moidName);
								
							try{	
								final Iterator filterIter = filterMap.keySet().iterator();
								while (filterIter.hasNext()) {
									final HashMap valMap = (HashMap) filterMap
											.get(filterIter.next());
									flexMeasFile.addData(valMap);
									saveMeasFile(flexMeasFile);
									/*try {
										flexMeasFile.saveData();
									} catch (final Exception e) {
										log.log(Level.FINEST,() ->ERROR_SAVE_MEAS_DATA  +e);
										e.printStackTrace();
										throw new SAXException(ERROR_SAVE_MEAS_DATA + e.getMessage(), e);
									}*/
								}
							}
							catch(final Exception e)
							{
								
								log.log(Level.FINEST,() -> "key is not found in flexMoidMap,for Key : " +moidName);
							}
							}
							closeMeasFile(flexMeasFile);
							/*
							if ((flexMeasFile != null) && flexMeasFile.isOpen()) {
								try {
									flexMeasFile.close();
								} catch (final Exception e) {
									log.log(Level.FINEST,() ->
											"ERROR_CLOSE_MEAS_FILE " +e);
									e.printStackTrace();
									throw new SAXException(
											ERROR_CLOSE_MEAS_FILE
													+ e.getMessage(), e);
								}
							}*/
						}

				}
				
				if (hasDynCounters) {

					DFormat dff = dfc.getFormatWithTagID(interfaceName,
							objectClassClone + dynPostfix);
					if ((dff != null&&!dynFlag) && (createOwnDynFile && dynMoidMap != null)) {
							dynFlag=true;
							log.log(Level.FINEST,() ->
									"Creating DYN MeasureMentFile for TagID "
											+(objectClassClone + dynPostfix));
							log.log(Level.FINEST,() ->
											"and  Interface Name "
											+interfaceName);
							dynMeasFile = openMeasFile(objectClassClone
												+ dynPostfix);
							/*try {

								dynMeasFile = Main.createMeasurementFile(
										sourceFile, objectClassClone
												+ dynPostfix, techPack,
										setType, setName, workerName, log);
							} catch (final Exception e) {
								log.log(Level.WARNING,
										ERROR_OPEN_MEAS_DATA, e);
								e.printStackTrace();
								throw new SAXException(
										ERROR_OPEN_MEAS_DATA
												+ e.getMessage(), e);
							}*/

							final Iterator<String> moidIter = moid.keySet()
									.iterator();
							while (moidIter.hasNext()) {
								final String moidName = moidIter.next();
								final Map<String, Map<String, String>> indexMap =  dynMoidMap.get(moidName);
								
							try{	
								final Iterator<String> indexIter = indexMap.keySet().iterator();
								while (indexIter.hasNext()) {
									final Map<String, String> valMap = indexMap
											.get(indexIter.next());
									dynMeasFile.addData(valMap);
									saveMeasFile(dynMeasFile);
									/*try {
										dynMeasFile.saveData();
									} catch (final Exception e) {
										log.log(Level.FINEST,() -> "ERROR_SAVE_MEAS_DATA " +e);
										e.printStackTrace();
										throw new SAXException(ERROR_SAVE_MEAS_DATA + e.getMessage(), e);
									}*/
								}
							}
							catch(final Exception e)
							{
								
								log.log(Level.FINEST,() -> "key is not found in dynMoidMap,for Key : "+moidName);
							}
							}
							closeMeasFile(dynMeasFile);
							/*
							if ((dynMeasFile != null) && dynMeasFile.isOpen()) {
								try {
									dynMeasFile.close();
								} catch (final Exception e) {
									log.log(Level.FINEST,() ->
											"ERROR_CLOSE_MEAS_FILE" +e);
									e.printStackTrace();
									throw new SAXException(
											ERROR_CLOSE_MEAS_FILE
													+ e.getMessage(), e);
								}
							}*/
						}

				}
				if (hasFlexDynCounters) {

					DFormat dff = dfc.getFormatWithTagID(interfaceName,
							objectClassClone + flexdynPostfix);
					if ((dff != null&&!flexDynFlag) && (createOwnFlexDynFile && flexDynMoidMap != null)) {
							flexDynFlag=true;
							log.log(Level.FINEST,() ->
									"Creating DYN MeasureMentFile for TagID "
											+(objectClassClone + flexdynPostfix));
							log.log(Level.FINEST,() ->
											"and  Interface Name "
											+interfaceName);
							flexDynMeasFile = openMeasFile(objectClassClone
												+ flexdynPostfix);

							final Iterator<String> moidIter = moid.keySet()
									.iterator();
							while (moidIter.hasNext()) {
								final String moidName = moidIter.next();
								final Map<String, Map<String, String>> indexMap =  flexDynMoidMap.get(moidName);
								
							try{	
								final Iterator<String> indexIter = indexMap.keySet().iterator();
								while (indexIter.hasNext()) {
									final Map<String, String> valMap = indexMap
											.get(indexIter.next());
									flexDynMeasFile.addData(valMap);
									saveMeasFile(flexDynMeasFile);
								}
							}
							catch(final Exception e)
							{
								
								log.log(Level.FINEST,() -> "key is not found in flexDynMoidMap,for Key : "+moidName);
							}
							}
							closeMeasFile(flexDynMeasFile);
						}

				}
				
				DFormat dfm = dfc.getFormatWithTagID(interfaceName,
						objectClassClone);

				if (dfm != null&&!mainFlag) {
					mainFlag=true;
					log.log(Level.FINEST,() -> "Creating MeasureMentFile for TagID "
							+objectClassClone); 
					log.log(Level.FINEST,() ->"and Interface Name "
							+interfaceName);
					
					measFile = openMeasFile(objectClassClone);
					/*try {

						measFile = Main.createMeasurementFile(sourceFile,
								objectClassClone, techPack, setType, setName,
								workerName, log);
					} catch (final Exception e) {
						log.log(Level.WARNING,
								ERROR_OPEN_MEAS_DATA, e);
						e.printStackTrace();
						throw new SAXException(
								ERROR_OPEN_MEAS_DATA
										+ e.getMessage(), e);
					}*/

					final Iterator<String> moidIter = moid.keySet().iterator();
					while (moidIter.hasNext()) {
						final String moidName = moidIter.next();
						final HashMap<String, String> counter = moid
								.get(moidName);

						log.log(Level.FINEST,() -> " saving from dataMap : moid: " +moidName); 
						log.log(Level.FINEST,() ->" loaderName : " +loaderName);
						measFile.addData(counter);
						measFile.addData(dataMap.get(moidName + loaderName));
						measFile.addData(counter);
						measFile.addData(dataMap.get(moidName + loaderName));
						saveMeasFile(measFile);
						/*try {
							measFile.saveData();
						} catch (final Exception e) {
							log.log(Level.FINEST,() ->
									"ERROR_SAVE_MEAS_DATA" +e);
							e.printStackTrace();
							throw new SAXException(
									ERROR_SAVE_MEAS_DATA
											+ e.getMessage(), e);

						}*/

					}
					closeMeasFile(measFile);
					/*
					if ((measFile != null) && measFile.isOpen()) {
						try {
							measFile.close();
						} catch (final Exception e) {
							log.log(Level.FINEST,() ->
									"ERROR_CLOSE_MEAS_FILE" +e);
							e.printStackTrace();
							throw new SAXException(
									ERROR_CLOSE_MEAS_FILE
											+ e.getMessage(), e);
						}
					}*/
				}
			  }
			  objectClassCloneList.clear();
			}
			vectorData.clear();
			measInfoAndLdn.clear();
			objectMapClone.clear();
			moidMap.clear();
			loaderClass.clear();
		}
		

	}

	private void addSaveMeasFile(MeasurementFile measFile) throws SAXException {
		measFile.addData(DC_SUSPECT_FLAG, suspectFlag);
		measFile.addData("MOID", measObjLdn);
		measFile.addData(addValues());
		saveMeasFile(measFile);
			
	}
	private MeasurementFile openMeasFile(String tagID) throws SAXException {
		MeasurementFile measFile = null;
		try {

			measFile = Main.createMeasurementFile(sourceFile,
					tagID, techPack, setType, setName,
					workerName, log);
		} catch (final Exception e) {
			log.log(Level.WARNING,
					ERROR_OPEN_MEAS_DATA, e);
			e.printStackTrace();
			throw new SAXException(
					ERROR_OPEN_MEAS_DATA
							+ e.getMessage(), e);
		}	
		return measFile;
	}	
	
	private void saveMeasFile(MeasurementFile measFile) throws SAXException {
		try {
			measFile.saveData();
		} catch (final Exception e) {
			log.log(Level.FINEST,() ->ERROR_SAVE_MEAS_DATA  +e);
			e.printStackTrace();
			throw new SAXException(ERROR_SAVE_MEAS_DATA + e.getMessage(), e);
		}
	}
	
	private int calculateMaxValue(Map<String, Map<String, String>> vectorBinValueMapForEachCounter){
		int maxval = 0;
		for (Map<String, String> valueMap : vectorBinValueMapForEachCounter.values()) {
			for (String i2 : valueMap.keySet()) {
				if (!i2.isEmpty() || i2 != null || !i2.equals("0")) {
					int binval = 0;
					if(i2.contains(":"))
						binval = Integer.parseInt(i2.split(":")[0]);
					else
						binval = Integer.parseInt(i2);
					if (binval > maxval) {
						maxval = binval;
					}
					/*
					if(!i2.contains(":"))
					{
					int binval = Integer.parseInt(i2);
						if (binval > maxval) {
							maxval = binval;
						}
					}
					else {
						int binval = Integer.parseInt(i2.split(":")[0]);
						if (binval > maxval) {
							maxval = binval;
						}
					}*/
				}
			}		
		}
		return maxval;
	}

	private HashSet<String> extractFilterNames(Map<String, Map<String, String>> vectorBinValueMapForEachCounter) {
		HashSet<String> filterNames = new HashSet<>();
		for (Map<String, String> valueMap : vectorBinValueMapForEachCounter.values()) {
			for (String i2 : valueMap.values()) {
				if (i2 != null) {
					if (i2.contains(":") && (i2.indexOf(':') != i2.length() - 1)) {
						filterNames.add(i2.split(":")[1]);
					}
				}
			}
		}
		return filterNames;
	}
	
	private int calculateMaxBinSize(Map<String, Map<String, String>> vectorBinValueMapForEachCounter){
		int maxBinSize = 0;
		for (Map<String, String> valueMap : vectorBinValueMapForEachCounter.values()) {
			int binSize = valueMap.size();
				if (binSize > maxBinSize) {
					maxBinSize = binSize;
				}
		}
		return maxBinSize;
	}
	
	private void closeMeasFile(MeasurementFile measFile) throws SAXException
	{
		if ((measFile != null) && measFile.isOpen()) {
			try {
				measFile.close();
			} catch (final Exception e) {
				log.log(Level.FINEST,() ->
						ERROR_CLOSE_MEAS_FILE +e);
				e.printStackTrace();
				throw new SAXException(
						ERROR_CLOSE_MEAS_FILE
						+ e.getMessage(), e);
			}
		}
	}
	private String calculateBegintime() {
		String result = null;
		try {
			String granPeriodETime = granularityPeriodEndTime;
			if (granPeriodETime.matches(".+\\+\\d\\d(:)\\d\\d")
					|| granPeriodETime.matches(".+\\-\\d\\d(:)\\d\\d")) {
				granPeriodETime = granularityPeriodEndTime.substring(0,
						granularityPeriodEndTime.lastIndexOf(":"))
						+ granularityPeriodEndTime
								.substring(granularityPeriodEndTime
										.lastIndexOf(":") + 1);
			}
			granPeriodETime = granPeriodETime.replaceAll("[.]\\d{3}", "");
			if (granPeriodETime.endsWith("Z")) {
				granPeriodETime = granPeriodETime.replaceAll("Z", "+0000");
			}

			final Date end = simpleDateFormat.parse(granPeriodETime);
			final Calendar cal = Calendar.getInstance();
			cal.setTime(end);
			final int period = Integer.parseInt(granularityPeriodDuration);
			cal.add(Calendar.SECOND, -period);
			result = simpleDateFormat.format(cal.getTime());
			log.log(Level.FINEST,"granPeriodETime",granPeriodETime);
			log.log(Level.FINEST,"format",cal.getTime());
		} catch (final ParseException e) {
			log.log(Level.WARNING, PARSER_FAILED_EXCEPTION, e);
		} catch (final NumberFormatException e) {
			log.log(Level.WARNING, PARSER_FAILED_EXCEPTION, e);
		} catch (final NullPointerException e) {
			log.log(Level.WARNING, PARSER_FAILED_EXCEPTION, e);
		} 
		return result;
	}

	public static void main(final String[] args) {
		int argnum = 0;
		Xml3GPP32435Parser np = null;
		FileInputStream fis = null;
		while (argnum < args.length) {
			if (args[argnum].equals("-sf")) {
				final String s = args[argnum + 1];
				final File f = new File(s);
				try(FileInputStream tempFIS = new FileInputStream(f)) {
					fis = tempFIS;
				} catch (final Exception e) {
					e.printStackTrace();
				}
				
			}

			argnum++;
		}
		if (fis == null) {
			final File f = new File("C:\\tmp\\koetus.xml");
			try(FileInputStream tempFIS = new FileInputStream(f)) {
					fis = tempFIS;
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		try {
			np = new Xml3GPP32435Parser();
			np.log = Logger.getLogger("etl.tp.st.sn.parser.NewParser.wn");

			np.parse(fis);
		} catch (final Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Extracts a substring from given string based on given regExp
	 * 
	 */
	public String parseFileName(final String str, final String regExp) {

		final Pattern pattern = Pattern.compile(regExp);
		final Matcher matcher = pattern.matcher(str);
		String result = "";

		if (matchObjectMaskAgainst.equalsIgnoreCase("whole")) {
			// Find a match between regExp and whole of str, and return group 1
			// as result
			if (matcher.matches()) {
				result = matcher.group(1);
				String logString = " regExp (" + regExp + ") found from " + str + "  :"
						+ result;
				log.finest(logString);
			} else {
				String warnString = "String " + str + " doesn't match defined regExp "
						+ regExp;
				log.warning(warnString);
			}
		} else {
			// Find a match between regExp and return group 1 as result.
			if (matcher.find()) {
				result = matcher.group(1);
				String logReg = " regExp (" + regExp + ") found from subset of "
						+ str + "  :" + result;
				log.finest(logReg);
			} else {
			String warnString = "No subset of String " + str
					+ " matchs defined regExp " + regExp;
			log.log(Level.WARNING,warnString);
				
				
			}
		}

		return result;

	}

	@Override
	public void characters(final char[] ch, final int start, final int length)
			throws SAXException {
		for (int i = start; i < (start + length); i++) {
			// If no control char
			if ((ch[i] != '\\') && (ch[i] != '\n') && (ch[i] != '\r')
					&& (ch[i] != '\t')) {
				charValue += ch[i];
			}
		}
	}

	/**
	 * This method handleVectorCounters is used to handle the vector counters.
	 * 
	 * @input Input to the handleVectorCounters is the
	 *        counterName,counterValues,MeasurementFile
	 * @return Output is a Map containing the bins and the value for bins.
	 */
	final private Map<String, String> handleVectorCounters(final Map datarow,
			 String counter, String filter, String value) {
		log.log(Level.FINEST,() -> "Filter passed : {} " +filter);
		final Map<String, String> tmpMap = new TreeMap<String, String>();
		final Map<String, Map<String, String>> tmpMap1 = new HashMap<String, Map<String, String>>();
		final Map newtmpMap = new HashMap();
		List<Integer> rangeindex = new ArrayList<Integer>();
		final Map compressvectorkeymap = new HashMap();
		final Map compressvectorvaluemap = new HashMap();
		final Map cmvectorvaluemap = new HashMap();
		try {
			int max = 0;
			String tmp = value;

			// get VECTOR counters
			final Set rangeCounters = ((Set) datarow.get("rangeCounters"));
			log.log(Level.FINEST,() -> "T:Range Counters Set : " +rangeCounters);
			
			Iterator iter = rangeCounters.iterator();
			while (iter.hasNext()) {

				final String key = (String) iter.next();
				log.log(Level.FINEST,() -> "T:Key: " +key);

				if (counter.equals(key)) {

					// BREAK-OUT THE RANGE (VECTOR) INTO AN ArrayList:
					final StringTokenizer tokens = new StringTokenizer(value,
							DELIMITER, true);
					List<String> bins = new ArrayList(tokens.countTokens() + 1); // We
																					// do
																					// not
																					// know
																					// capacity
																					// needed,
																					// so
																					// make
																					// it
																					// bigger
																					// then
																					// needed
																					// (and
																					// reduce
																					// later).
					boolean prevTokenWasDelim = true;
					String currentToken;
					while (tokens.hasMoreTokens()) {
						currentToken = tokens.nextToken();
						if (!currentToken.equalsIgnoreCase(DELIMITER)) {
							if (currentToken.equalsIgnoreCase("Nil") || currentToken.trim().equalsIgnoreCase("") || currentToken.equalsIgnoreCase("NULL")) {
								currentToken = "";
								log.finest("delimiter:: " + DELIMITER);
							}
							bins.add(currentToken); // It's not a delimiter so
													// add it.
							prevTokenWasDelim = false;
						} else if (!prevTokenWasDelim) {
							prevTokenWasDelim = true; // It's a delimiter so we
														// don't add anything
						} else {
							bins.add(null); // It's a delimiter AND SO WAS THE
											// LAST ONE. This represents empty
											// input - so we add null.
						}
					}
					if (prevTokenWasDelim) { // This accounts for empty bin at
												// end of vector.
						bins.add(null);
					}
					((ArrayList) bins).trimToSize(); // Set the capacity of the
														// ArrayList to it's
														// size.

					// DECOMPRESS THE VECTOR IF REQUIRED:
					if (compressedVectorCounters.contains(key)) {
						rangeindex = getrangeindexfromcompressVector(bins);
						bins = getvaluesfromcompressVector(bins);
						compressvectorkeymap.put(key, rangeindex);
						if ((null == bins) || (null == rangeindex)) {
							log.log(Level.FINEST,() ->VECTOR_NO_DATA + key);
							datarow.put(key, null);
							continue;
						}
					}
					
					
					newtmpMap.put(key, bins);

					// IS IT THE LONGEST VECTOR SO FAR?
					if (bins.size() > max) {
						max = bins.size();
					}

					// IF IT'S REQUIRED, INSERT THE 1ST VALUE OF VECTOR (THE
					// ZERO INDEX) INTO datarow
					if (!tmpMap.isEmpty()) {
						datarow.put(key, bins.get(0));
					}
					iter = newtmpMap.keySet().iterator();
					List<String> oldcompressvectorlist;
					TreeSet<Integer> keylist = new TreeSet<>();
					int compressvectorlen = -1;
					List<Integer> compressvectorkeylist;
					// loop all range counters (columns)
					while (iter.hasNext()) {

						final String key1 = (String) iter.next();

						if (compressedVectorCounters.contains(key1)) {
							oldcompressvectorlist = (List) newtmpMap.get(key1);
							compressvectorkeylist = (List) compressvectorkeymap
									.get(key1);
							keylist.addAll(compressvectorkeylist);
							// Instanciate ArrayList to be returned (with
							// required capacity) and fill it with zeros
							List<String> result = new ArrayList<>();
							
							
							
							
							// Add the values from input into the correct
							// position in the returned ArrayList (according to
							// their corresponding index)
							
							for (int i = 0; i < oldcompressvectorlist.size(); i++) {
								tmpMap.put(compressvectorkeylist.get(i) + "",
										oldcompressvectorlist.get(i));
							}
							if (result.size() > compressvectorlen) {
								compressvectorlen = result.size();
							}
							
							newtmpMap.put(key1, result);
							tmpMap1.put(key1, tmpMap);
							compressvectorvaluemap.put(key1,
									oldcompressvectorlist);
						} else {
							for (int i = 0; i < max; i++) {
								tmpMap.put(i + "", bins.get(i));
							}
						}
					}

					

				}

			}

			// / get CMVECTOR counters
			final Set cmVectorCounters = ((Set) datarow.get("cmVectorCounters"));
			log.log(Level.FINEST,() -> "CM Counters Set : " +cmVectorCounters);

			// loop all CMVECTOR counters in this datarow
			Iterator cmIter = cmVectorCounters.iterator();
			while (cmIter.hasNext()) {
				final String key1 = (String) cmIter.next();
				if (counter.equals(key1)) {
					// musta add one delim to the end to make it work...
					tmp = tmp.concat(DELIMITER);
					int i = 0;
					boolean prewWasDelim = true;
					final StringTokenizer token = new StringTokenizer(tmp,
							DELIMITER, true);
					
					List<String> bins = new ArrayList(token.countTokens() + 1);
					log.finest("Iterating through the whil for bins......");
					while (token.hasMoreTokens()) {

						final String tmptoken = token.nextToken();
						String tmpvalue = null;
						log.log(Level.FINEST,() ->"Printing the token " +tmptoken);
						if (prewWasDelim || (tmptoken.equalsIgnoreCase(DELIMITER) && prewWasDelim)) {
							if (!tmptoken.equalsIgnoreCase(DELIMITER)) {
								if (tmptoken.equalsIgnoreCase("Nil") || tmptoken.trim().equalsIgnoreCase("") || tmptoken.equalsIgnoreCase("NULL") ) {
									log.finest("Nil or space has been occured.....");
									tmpvalue = "";
								} else {
									tmpvalue = tmptoken;
								}
							}

							log.log(Level.FINEST,() ->"Value added to bins " +tmptoken);
							bins.add(tmpvalue);
							i++;
						}

						prewWasDelim = false;
						if (tmptoken.equalsIgnoreCase(DELIMITER)) {
							prewWasDelim = true;
						}
					}

					if (cmVectorCounters.contains(key1)) {
						String element = key1 + "  " + bins;
						log.log(Level.FINEST,() ->"In new tmp map " +element);
						newtmpMap.put(key1, bins);
						if ((null == bins) || (null == rangeindex)) {
							log.log(Level.FINEST,() ->VECTOR_NO_DATA + key1);
							datarow.put(key1, null);
							continue;
						}

					}

					if (!bins.isEmpty()) {
						log.finest("To set null at 0 index");
						// put the value from the first (zero) index into the
						// original datarow because CMVECTORs are on hand
						counterValueMap.put(
								(String) measNameMap.get(measValueIndex),
								bins.get(0));
						bins.set(0, null);
					}

					iter = newtmpMap.keySet().iterator();

					List<String> oldcmvectorlist;
					int cmvectorlen = -1;

					while (iter.hasNext()) {
						final String key2 = (String) iter.next();
						if (cmVectorCounters.contains(key2)) {
							oldcmvectorlist = (List) newtmpMap.get(key2);

							// Instanciate ArrayList to be returned (with
							// required capacity) and fill it with zeros
							List<String> result = new ArrayList<>();

							for (int i2 = 1; i2 < oldcmvectorlist.size(); i2++) {
								tmpMap.put(i2 + "",
										 oldcmvectorlist.get(i2));
								String logString = "Printing the final map  " + i2 + "  "
										+ oldcmvectorlist.get(i2);
								log.finest(logString);
							}

							if (result.size() > cmvectorlen) {
								cmvectorlen = result.size();
							}

							newtmpMap.put(key1, result);
							tmpMap1.put(key1, tmpMap);
							cmvectorvaluemap.put(key1, oldcmvectorlist);
						} else {
							for (int i1 = 0; i1 < max; i1++) {
								tmpMap.put(i1 + "", (String) bins.get(i1));

							}
						}
					}

				}

			}
			
			/*MyCodeStart*/
			// get Flex VECTOR counters
			final Set<String> flexRangeCounters = ((Set) datarow.get(FLEX_RANGE_COUNTERS));
			log.log(Level.FINEST,() -> "T:flexRangeCounters Set : " +flexRangeCounters);
			Iterator<String> flexIter = flexRangeCounters.iterator();
			while (flexIter.hasNext()) {

				final String key =  flexIter.next();
				log.log(Level.FINEST,() -> "T:Key: " +key);

				if (counter.equals(key)) {

					// BREAK-OUT THE RANGE (VECTOR) INTO AN ArrayList:
					final StringTokenizer tokens = new StringTokenizer(value, DELIMITER, true);
					List<String> bins = new ArrayList<>(tokens.countTokens() + 1);
					boolean prevTokenWasDelim = true;
					String currentToken;
					while (tokens.hasMoreTokens()) {
						currentToken = tokens.nextToken();
						if (!currentToken.equalsIgnoreCase(DELIMITER)) {
							if (currentToken.equalsIgnoreCase("Nil") || currentToken.trim().equalsIgnoreCase("") || currentToken.equalsIgnoreCase("NULL")) {
								currentToken = "";
								log.finest("delimiter:: " + DELIMITER);
							}
							bins.add(currentToken); // It's not a delimiter 
							prevTokenWasDelim = false;
						} else if (!prevTokenWasDelim) {
							prevTokenWasDelim = true; // It's a delimiter 
						} else {
							bins.add(null); 
						}
					}
					
					if (prevTokenWasDelim) { // This accounts for empty bin at
						// end of vector.
						bins.add(null);
					}
					((ArrayList) bins).trimToSize(); 
						
					// DECOMPRESS THE VECTOR IF REQUIRED:
					if (flexCompVectors.contains(key)) {
					rangeindex = getrangeindexfromcompressVector(bins);
					bins = getvaluesfromcompressVector(bins);
					compressvectorkeymap.put(key, rangeindex);
					if ((null == bins) || (null == rangeindex)) {
						log.log(Level.FINEST,() ->VECTOR_NO_DATA +key);
						datarow.put(key, null);
						continue;
						}
					}
					
					
					newtmpMap.put(key, bins);
					// IS IT THE LONGEST VECTOR SO FAR?
					if (bins.size() > max) {
						max = bins.size();
					}
					// IF IT'S REQUIRED, INSERT THE 1ST VALUE OF VECTOR (THE
					// ZERO INDEX) INTO datarow
					if (!tmpMap.isEmpty()) {
						datarow.put(key, bins.get(0));
					}
					iter = newtmpMap.keySet().iterator();
					List<String> oldcompressvectorlist;
					TreeSet<Integer> keylist = new TreeSet<Integer>();
					int compressvectorlen = -1;
					List<Integer> compressvectorkeylist;
					// loop all range counters (columns)
					while (iter.hasNext()) {
						final String key1 = (String) iter.next();
						if (flexCompVectors.contains(key1)) {
							oldcompressvectorlist = (List) newtmpMap.get(key1);
							compressvectorkeylist = (List) compressvectorkeymap
									.get(key1);
							keylist.addAll(compressvectorkeylist);
							List<String> result = new ArrayList();
							
							
										
							for (int i = 0; i < oldcompressvectorlist.size(); i++) {
								tmpMap.put(compressvectorkeylist.get(i) + ":"+filter,
										 oldcompressvectorlist.get(i)+":"+filter);
							}
							if (result.size() > compressvectorlen) {
								compressvectorlen = result.size();
							}
							newtmpMap.put(key1, result);
							tmpMap1.put(key1, tmpMap);
							compressvectorvaluemap.put(key1,
									oldcompressvectorlist);
						} else {
							for (int i = 0; i < max; i++) {
								tmpMap.put(i + ":"+filter, bins.get(i)+":"+filter);
							}
						}
					}

				} 
			}
			/*MyCodeEnd*/
		}catch (NullPointerException ne) {
			log.log(Level.FINEST,() -> "Counter values are not present in database");
		} 
		return tmpMap;
	}

	public Map addValues() {
		measurement = new HashMap();
		measurement.put("PERIOD_DURATION", granularityPeriodDuration);
		log.log(Level.FINEST,() -> "PERIOD_DURATION: " +granularityPeriodDuration);
		measurement.put("repPeriodDuration", repPeriodDuration);
		log.log(Level.FINEST,() -> "repPeriodDuration: " +repPeriodDuration);
		final String begin = calculateBegintime();
		if (begin != null) {
			measurement.put("DATETIME_ID", begin);
			log.log(Level.FINEST,() -> "DATETIME_ID: " +begin);
		}
		measurement.put("collectionBeginTime", collectionBeginTime);
		log.log(Level.FINEST,() -> "collectionBeginTime: " +collectionBeginTime);
		
		measurement.put(FILE_NAME, (sourceFile == null ? DUMMY_FILE
				: sourceFile.getName()));
		String dummyFile = (sourceFile == null ? DUMMY_FILE : sourceFile.getName());
		log.log(Level.FINEST,() -> "filename: " +dummyFile);
		measurement.put(JVM_TIME_ZONE, JVM_TIMEZONE);
		log.log(Level.FINEST,() -> "JVM_TIMEZONE: " + JVM_TIMEZONE);
		measurement.put(DIR_NAME, (sourceFile == null ? DUMMY_DIR
				: sourceFile.getDir()));
		String dummyDir = (sourceFile == null ? DUMMY_DIR : sourceFile.getDir());
		log.log(Level.FINEST,() -> "DIRNAME: " +dummyDir);
		measurement.put(MEAS_INFO_ID, measInfoId);
		log.log(Level.FINEST,() -> "measInfoId: " +measInfoId);
		measurement.put("objectClass", objectClass);
		log.log(Level.FINEST,() -> "objectClass: " +objectClass);
		measurement.put(VENDOR_NAME, vendorName);
		log.log(Level.FINEST,() -> "vendorName: " +vendorName);
		measurement.put(FILE_FORMAT_VERSION, fileFormatVersion);
		log.log(Level.FINEST,() -> "fileFormatVersion: " +fileFormatVersion);
		measurement.put(DN_PREFIX, dnPrefix);
		log.log(Level.FINEST,() -> "dnPrefix: " +dnPrefix);
		measurement.put(LOCAL_DN, fsLocalDN);
		log.log(Level.FINEST,() -> "localDn: " +fsLocalDN);
		measurement.put("managedElementLocalDn", meLocalDN);
		log.log(Level.FINEST,() -> "managedElementLocalDn: " +meLocalDN);
		measurement.put(ELEMENT_TYPE, elementType);
		log.log(Level.FINEST,() -> "elementType: " +elementType);
		measurement.put(USER_LABEL, userLabel);
		log.log(Level.FINEST,() -> "userLabel: " +userLabel);
		measurement.put(SW_VERSION, swVersion);
		log.log(Level.FINEST,() -> "swVersion: " +swVersion);
		measurement.put(END_TIME, granularityPeriodEndTime);
		log.log(Level.FINEST,() -> "endTime: " +granularityPeriodEndTime);
		measurement.put(JOB_ID, jobId);
		log.log(Level.FINEST,() -> "jobId: " +jobId);
		
		measurement.put(FILE_NAME,(sourceFile == null ? DUMMY_FILE : sourceFile.getName()));
		
		measurement.put(JVM_TIME_ZONE, JVM_TIMEZONE);
		
		if(sourceFile!=null && sourceFile.getDir()!=null) {
			measurement.put(DIR_NAME, sourceFile.getDir());
		}
		

		return measurement;
	}

	public List getrangeindexfromcompressVector(List input) {

		if (null == input || input.get(0) == null) { // HQ59381 fix for null
														// pointer exception
			return null;
		}

		int inputSize = input.size();

		if (input.isEmpty() || (input.get(0).equals("") && inputSize == 1)) {
			return input; // Return input List if it is empty
		}

		final int expectedNumOfPairs;
		try {
			expectedNumOfPairs = Integer.parseInt((String) input.get(0)); // Find
																			// how
																			// many
																			// index
																			// value
																			// pairs
																			// there
																			// are
																			// in
																			// List.
		} catch (Exception e) {
			return null;
		}

		if (expectedNumOfPairs > 1024) { // Make sure it's not too big
			return null;
		}
		if (expectedNumOfPairs == 0 && inputSize == 1) {
			return null; // return input if it just has zero
		}
		if (inputSize % 2 == 0) { // Make sure it has odd size
			return null;
		}
		if (inputSize != (expectedNumOfPairs * 2) + 1) { // Check for correct
															// num of name-value
															// pairs. This also
															// catches negative
															// expectedNumOfPairs
			return null;
		}

		// This FOR loop does 3 things: sanity checks the indecies, finds the
		// highest one and puts them in their own ArrayList,
		int index;
		int highestIndex = -1;
		List<Integer> indecies = new ArrayList<>(expectedNumOfPairs);
		for (int i = 1; i < inputSize; i = i + 2) { // Takes the indecies from
													// input List and put the in
													// their own list
			try {
				index = Integer.parseInt((String) input.get(i));

			} catch (Exception e) {
				return null;
			}
			if (index < 0) {
				return null;
			}
			indecies.add(index);
			if (index > highestIndex) {
				highestIndex = index;
			}
		}
		if (highestIndex > 1024) { // Make sure highest index is not too big
			return null;
		}

		return indecies;
	}

	/**
	 * This method decompresses (decodes) a compressed vector.
	 * 
	 * @input A compressed vector as a List: first entry in list indicates
	 *        number of indexes, and following entries are alternatly an index
	 *        and a value.
	 * @return A decompressed (decoded) version of the input as a List,
	 *         containing the values in their corect position.
	 */
	public List getvaluesfromcompressVector(List input) {

		if (null == input || input.get(0) == null) { // HQ59381 fix for null
														// pointer exception
			return null;
		}

		int inputSize = input.size();

		if (input.isEmpty() || (input.get(0).equals("") && inputSize == 1)) {
			return input; // Return input List if it is empty
		}

		final int expectedNumOfPairs;
		try {
			expectedNumOfPairs = Integer.parseInt((String) input.get(0)); // Find
																			// how
																			// many
																			// index
																			// value
																			// pairs
																			// there
																			// are
																			// in
																			// List.
		} catch (Exception e) {
			return null;
		}

		if (expectedNumOfPairs > 1024) { // Make sure it's not too big
			return null;
		}
		if (expectedNumOfPairs == 0 && inputSize == 1) {
			return input; // return input if it just has zero
		}
		if (inputSize % 2 == 0) { // Make sure it has odd size
			return null;
		}
		if (inputSize != (expectedNumOfPairs * 2) + 1) { // Check for correct
															// num of name-value
															// pairs. This also
															// catches negative
															// expectedNumOfPairs
			return null;
		}

		// This FOR loop does 3 things: sanity checks the indecies, finds the
		// highest one and puts them in their own ArrayList,
		int index;
		int highestIndex = -1;
		List<Integer> indecies = new ArrayList(expectedNumOfPairs);
		for (int i = 1; i < inputSize; i = i + 2) { // Takes the indecies from
													// input List and put the in
													// their own list
			try {
				index = Integer.parseInt((String) input.get(i));

			} catch (Exception e) {
				return null;
			}
			if (index < 0) {
				return null;
			}
			indecies.add(index);
			if (index > highestIndex) {
				highestIndex = index;
			}
		}
		if (highestIndex > 1024) { // Make sure highest index is not too big
			return null;
		}

		// Instanciate ArrayList to be returned (with required capacity) and
		// fill it with zeros
		List<String> result = new ArrayList(highestIndex + 1);
		

		// Add the values from input into the correct position in the returned
		// ArrayList (according to their corresponding index)
		for (int i = 0; i < indecies.size(); i++) {
			result.add((String) input.get(i * 2 + 2));
		}

		return result;
	}
	
	/*MyCodeStart
	 * Checks if a given counter is a Flex Vector / Flex Compressed Vector Counter
	 * 
	 */
	private boolean checkIfFlexVector(String counterName) {
		String counter = "";
		if (counterName.contains("_")) {
			String[] flexSplit = counterName.split("_");
			counter = flexSplit[0];
			if (((Set) vectorMeasurement.get(FLEX_RANGE_COUNTERS)).contains(counter)) {
				containsFilter = true;
				return true;
			}
		} else {
			counter = counterName;
			if (((Set) vectorMeasurement.get(FLEX_RANGE_COUNTERS)).contains(counter)) {
				containsFilter = false;
				return true;
			}
		}
		return false;
	}
	/*MyCodeEnd*/
	
	private boolean checkIfFlexDyn(String counterName) {
		List<String> dynPartList = new ArrayList<>();
		List<String> dynIndexList = new ArrayList<>();
		String counter = "";
		String flexDynFilter = "";
		if(counterName.contains("_"))
		{
			String[] filterNames = counterName.split("_");
			counter = filterNames[0];
			flexDynFilter = filterNames[1];
		}
		else {
			counter = counterName;
		}
		for(int i=0;i<multiDynMaskCount;i++) {
			DynMaskConfig dynMaskConfig = new DynMaskConfig(multiDynMaskConfig.get(i));
			flexDynMask = dynMaskConfig.getDynMask();
			Matcher m = getMatcher(counter, flexDynMask);
			if (m.matches())
			{
				String staticPart = m.group(dynMaskConfig.getStaticPart());
				if(((Set)flexDynCounterBin.get(FLEX_DYN_COUNTERS)).contains(staticPart))
				{
							for(int j=0;j<dynMaskConfig.getPartIndexList().size();j++) {
								dynPartList.add(m.group(dynMaskConfig.getPartIndexList().get(j).getPartIndex()));
								dynIndexList.add(m.group(dynMaskConfig.getPartIndexList().get(j).getIndexIndex()));
							}
								handleFlexDyn(dynPartList,dynIndexList, staticPart,flexDynFilter, counterName);
						return true;
				}
			}
		}
			return false;
	}


	/**
	 * Checks if a given counter is a Flex Counter
	 * 
	 */
	private boolean checkIfFlex(String counterName) {
		String counter = "";
		if (counterName.contains("_")) {
			String[] flexSplit = counterName.split("_");
			counter = flexSplit[0];
			if (((Set) flexCounterBin.get(FLEX_COUNTERS)).contains(counter)) {
				handlesFlex(true, counterName);
				return true;
			}
		} else {
			counter = counterName;
			if (((Set) flexCounterBin.get(FLEX_COUNTERS)).contains(counter)) {
				handlesFlex(false, counterName);
				return true;
			}
		}
		return false;
	}
	
	private boolean checkIfDyn(String counterName) {
		String counter = "";
		Matcher m = getMatcher(counterName, dynMask);
		if (m.matches()) {
			String dynPart = m.group(dynPartIndex);
			String dynIndex = m.group(dynIndexIndex);
			String staticPart = m.group(staticPartIndex);
			if(((Set)dynCounterBin.get("dynCounters")).contains(staticPart)) {
				handleDyn(dynPart, dynIndex, staticPart);
				return true;
			}
		}
		return false;
	}
	
	//EQEV-91982 NIL is handled for dynamic counters to convert NIL into NULL
	private void handleDyn(String dynPart, String dynIndex, String staticPart) {
		String counterVal = charValue;
		if ((counterVal != null) && (counterVal.equalsIgnoreCase("NIL") || counterVal.trim().equalsIgnoreCase("") ||counterVal.equalsIgnoreCase("NULL") ))
		  { 
			  counterVal = ""; 
			  log.finest("Setting the value to null as in-valid dynamic data being received from the Node"); 
		  }
		Map dynValueMap = dynIndexMap.get(dynIndex);
		if (dynValueMap == null) {
			dynValueMap = new HashMap<String, String>();
			dynValueMap.putAll(addValues());
		}
		dynValueMap.put(dynPart, dynIndex);
		dynValueMap.put(staticPart, counterVal);
		dynValueMap.put(DC_SUSPECT_FLAG, suspectFlag);
		dynValueMap.put("MOID", measObjLdn);
		dynIndexMap.put(dynIndex, dynValueMap);
	}

    private void handleFlexDyn(List<String> dynPartList, List<String> dynIndexList, String staticPart,String flexDynFilter, String counterName) {
        String counterVal = charValue;
        String flexHash = "";
        if(null!=flexDynFilter)
        {
        	flexHash = Integer.toString(flexDynFilter.hashCode());
        }
        else {
        	flexHash = Integer.toString(counterName.hashCode());
        }
        if ((counterVal != null) && (counterVal.equalsIgnoreCase("NIL") || counterVal.trim().equalsIgnoreCase("") ||counterVal.equalsIgnoreCase("NULL") ))
        {                          
            counterVal = "";
        }
        String dynIndexMapKey = "";
        for (int ii = 0; ii < dynPartList.size(); ii++) {
                dynIndexMapKey = dynIndexMapKey + dynPartList.get(ii) + dynIndexList.get(ii) + flexDynFilter;
        }
        Map dynValueMap = flexDynIndexMap.get(dynIndexMapKey);
        if (dynValueMap == null) {
            dynValueMap = new HashMap<>();
            dynValueMap.putAll(addValues());
            flexDynIndexMap.put(dynIndexMapKey, dynValueMap);
        }
        for (int ii = 0; ii < dynPartList.size(); ii++) {
            dynValueMap.put(dynPartList.get(ii), dynIndexList.get(ii));
        }
        dynValueMap.put(staticPart, counterVal);
        dynValueMap.put(DC_SUSPECT_FLAG, suspectFlag);
        dynValueMap.put("MOID", measObjLdn);
        dynValueMap.put(FLEX_FILTERNAME, flexDynFilter);
        dynValueMap.put(FILTER_HASH, flexHash);
        log.log(Level.FINEST,"flexDynFilter is:{0}",flexDynFilter);
        log.log(Level.FINEST,"Final dynValueMap after adding all values is:{0}",dynValueMap);
        log.log(Level.FINEST,"Final dynIndexMap after adding all values is:{0}",flexDynIndexMap);
}
	/**
	 * Handles Flex counters and populates flex counter map
	 * 
	 */

	private void handlesFlex(boolean hasFilter, String flexCounterFilter) {
		try {
			String flexCounter = "";
			String flexFilter = "";
			String flexHash = "";
			String origValue = charValue;
			if (hasFilter) {
				Matcher m = getMatcher(flexCounterFilter, FLEX_COUNTER_PATTERN);
				if (m.matches()) {
					if (m.groupCount() > 1) {
						flexCounter = m.group(1);
						flexFilter = m.group(2);
					} else {
						flexCounter = flexCounterFilter;
					}
				}
			} else {
				flexCounter = flexCounterFilter;
			}
			flexHash = Integer.toString(flexFilter.hashCode());
			
			

			if ((origValue != null)
					&& (origValue.equalsIgnoreCase("NIL") || origValue.trim()
							.equalsIgnoreCase("") || origValue.equalsIgnoreCase("NULL"))) {
				origValue = "";
				log.finest("Setting the value to null as in-valid data being received from the Node");
			}

			if (!flexFilterMap.isEmpty() && flexFilterMap.containsKey(flexHash)) {

				flexValueMap = (HashMap) flexFilterMap.get(flexHash);
				

				flexValueMap.put(flexCounter, origValue);
			} else {
				flexValueMap = new HashMap();
				flexValueMap = (HashMap) addValues();
				flexValueMap.put(DC_SUSPECT_FLAG, suspectFlag);
				flexValueMap.put("MOID", measObjLdn);
				flexValueMap.put(FLEX_FILTERNAME, flexFilter);
				flexValueMap.put(FILTER_HASH, flexHash);
				flexValueMap.put(flexCounter, origValue);
			}
			
			flexFilterMap.put(flexHash, flexValueMap);
			
		} catch (Exception e) {
			log.info("Exception occured trace below" + e.getMessage());
			e.getStackTrace();

		}
	}
}
	class PartIndexPair {
		private	int partIndex;
		private	int indexIndex;
		

	public PartIndexPair(int partIndex, int indexIndex) {
		super();
		this.partIndex = partIndex;
		this.indexIndex = indexIndex;
	}
		@Override
		public String toString() {
			return "PartIndex [partIndex=" + partIndex + ", indexIndex=" + indexIndex + "]";
		}

		public PartIndexPair(String input) {
			String[] split = input.split(",");

			partIndex = Integer.parseInt(split[0]);
			indexIndex = Integer.parseInt(split[1]);
			
		}

		public int getPartIndex() {
			return partIndex;
		}

		public void setPartIndex(int partIndex) {
			this.partIndex = partIndex;
		}

		public int getIndexIndex() {
			return indexIndex;
		}

		public void setIndexIndex(int indexIndex) {
			this.indexIndex = indexIndex;
		}
	}

	class DynMaskConfig {
		private String dynMask;
		private int staticPart;
		private List<PartIndexPair> partIndexList = new ArrayList<>();

		public DynMaskConfig(String dynMask, int staticPart, List<PartIndexPair> partIndex) {
			super();
			this.dynMask = dynMask;
			this.staticPart = staticPart;
			this.partIndexList = partIndex;
		}

		public String getDynMask() {
			return dynMask;
		}

		public void setDynMask(String dynMask) {
			this.dynMask = dynMask;
		}

		public int getStaticPart() {
			return staticPart;
		}

		public void setStaticPart(int staticPart) {
			this.staticPart = staticPart;
		}

		public List<PartIndexPair> getPartIndexList() {
			return partIndexList;
		}

		public void setPartIndexList(List<PartIndexPair> partIndexList) {
			this.partIndexList = partIndexList;
		}

		@Override
		public String toString() {
			return "DynMaskConfig [dynMask=" + dynMask + ", staticPart=" + staticPart + ", partIndexList=" + partIndexList
					+ "]";
		}

		public DynMaskConfig(String input) {
			String[] split = input.split("::");
			dynMask = split[0];
			staticPart = Integer.parseInt(split[1]) ;

			for (int i = 2; i < split.length; i++) {
				PartIndexPair p1 = new PartIndexPair(split[i]);
				partIndexList.add(p1);
				
			}
		}

	}