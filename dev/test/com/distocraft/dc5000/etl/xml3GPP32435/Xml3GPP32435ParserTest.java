package com.distocraft.dc5000.etl.xml3GPP32435;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import ssc.rockfactory.RockFactory;

import com.distocraft.dc5000.common.SessionHandler;
import com.distocraft.dc5000.common.StaticProperties;
import com.distocraft.dc5000.etl.engine.common.EngineCom;
import com.distocraft.dc5000.etl.parser.Main;
import com.distocraft.dc5000.etl.parser.MeasurementFile;
import com.distocraft.dc5000.etl.parser.MeasurementFileImpl;
import com.distocraft.dc5000.etl.parser.ParseSession;
import com.distocraft.dc5000.etl.parser.ParserDebugger;
import com.distocraft.dc5000.etl.parser.SourceFile;
import com.distocraft.dc5000.etl.parser.TransformerCache;
import com.distocraft.dc5000.repository.cache.DFormat;
import com.distocraft.dc5000.repository.cache.DItem;
import com.distocraft.dc5000.repository.cache.DataFormatCache;
import java.lang.reflect.*;
//import com.ericsson.junit.HelpClass;

/**
 * 
 * @author ejarsok
 *
 */

public class Xml3GPP32435ParserTest {

	Xml3GPP32435Parser objectUnderTest;

	private static Method getSeconds;
	private Map vectorMeasurement;
	private static Method endDocument;
	private static Method handleTAGmoid;
	private static Method checkIfFlexVector;
	private static Method calculateBegintime;
	private static Method add;
	private static Method extractCounterName;
	private static Method getMatcher;
	private static Method checkIfFlex;
	private static Method checkIfDyn;
	private static Field mainParserObject;
	private static Method handleDyn;
	private static Field techPack;
	private static Method handlesFlex;
	private static Field setType;
	private static Method parseFileName;
	private static Field setName;
	private static Method getDataIDFromProcessInstructions;
	private static Field status;
	private static Method startElement;
	private static Field flexCounterBin;

	private static Field workerName;

	private static Field charValue;

	private static Field errorList;

	private static Field fileFormatVersion;

	private static Field vendorName;

	private static Field dnPrefix;

	private static Field fsLocalDN;

	private static Field elementType;

	private static Field collectionBeginTime;

	private static Field measNameMap;

	private static Field clusterMap;

	private static Field origMeasNameMap;

	private static Field meLocalDN;

	private static Field userLabel;

	private static Field swVersion;

	private static Field measInfoId;

	private static Field jobId;

	private static Field granularityPeriodDuration;

	private static Field granularityPeriodEndTime;

	private static Field repPeriodDuration;

	private static Field measIndex;

	private static Field measValueIndex;

	private static Field objectClass;

	private static Field readVendorIDFrom;

	private static Field fillEmptyMoidStyle;

	private static Field fillEmptyMoidValue;

	private static Field objectMask;

	private static Field sourceFile;

	private static Field measObjLdn;

	private static Field oldObjClass;

	private static Field suspectFlag;

	private static Field measFile;

	private static Constructor sourceFileC;

	private static Constructor MeasurementFileImplC;

	private static Constructor ParseSessionC;

	// MeasurementFileImpl
	private static Field data;

	// Main
	private static Field fileList;

	private static Field psession;

//  @BeforeClass
//  public static void init() {
//    
//    try {
//      getSeconds = Xml3GPP32435Parser.class.getDeclaredMethod("getSeconds", new Class[] {String.class});
//      handleTAGmoid = Xml3GPP32435Parser.class.getDeclaredMethod("handleTAGmoid", new Class[] {String.class});
//      calculateBegintime = Xml3GPP32435Parser.class.getDeclaredMethod("calculateBegintime", new Class[] {});
//      mainParserObject = Xml3GPP32435Parser.class.getDeclaredField("mainParserObject");
//      techPack = Xml3GPP32435Parser.class.getDeclaredField("techPack");
//      setType = Xml3GPP32435Parser.class.getDeclaredField("setType");
//      setName = Xml3GPP32435Parser.class.getDeclaredField("setName");
//      status = Xml3GPP32435Parser.class.getDeclaredField("status");
//      workerName = Xml3GPP32435Parser.class.getDeclaredField("workerName");
//      charValue = Xml3GPP32435Parser.class.getDeclaredField("charValue");
//      errorList = Xml3GPP32435Parser.class.getDeclaredField("errorList");
//      fileFormatVersion = Xml3GPP32435Parser.class.getDeclaredField("fileFormatVersion");
//      vendorName = Xml3GPP32435Parser.class.getDeclaredField("vendorName");
//      dnPrefix = Xml3GPP32435Parser.class.getDeclaredField("dnPrefix");
//      fsLocalDN = Xml3GPP32435Parser.class.getDeclaredField("fsLocalDN");
//      elementType = Xml3GPP32435Parser.class.getDeclaredField("elementType");
//      collectionBeginTime = Xml3GPP32435Parser.class.getDeclaredField("collectionBeginTime");
//      measNameMap = Xml3GPP32435Parser.class.getDeclaredField("measNameMap");
//      clusterMap = Xml3GPP32435Parser.class.getDeclaredField("clusterMap");
//      origMeasNameMap = Xml3GPP32435Parser.class.getDeclaredField("origMeasNameMap");
//      meLocalDN = Xml3GPP32435Parser.class.getDeclaredField("meLocalDN");
//      userLabel = Xml3GPP32435Parser.class.getDeclaredField("userLabel");
//      swVersion = Xml3GPP32435Parser.class.getDeclaredField("swVersion");
//      measInfoId = Xml3GPP32435Parser.class.getDeclaredField("measInfoId");
//      jobId = Xml3GPP32435Parser.class.getDeclaredField("jobId");
//      granularityPeriodDuration = Xml3GPP32435Parser.class.getDeclaredField("granularityPeriodDuration");
//      granularityPeriodEndTime = Xml3GPP32435Parser.class.getDeclaredField("granularityPeriodEndTime");
//      repPeriodDuration = Xml3GPP32435Parser.class.getDeclaredField("repPeriodDuration");
//      measIndex = Xml3GPP32435Parser.class.getDeclaredField("measIndex");
//      measValueIndex = Xml3GPP32435Parser.class.getDeclaredField("measValueIndex");
//      objectClass = Xml3GPP32435Parser.class.getDeclaredField("objectClass");
//      readVendorIDFrom = Xml3GPP32435Parser.class.getDeclaredField("readVendorIDFrom");
//      fillEmptyMoidStyle = Xml3GPP32435Parser.class.getDeclaredField("fillEmptyMoidStyle");
//      fillEmptyMoidValue = Xml3GPP32435Parser.class.getDeclaredField("fillEmptyMoidValue");
//      objectMask = Xml3GPP32435Parser.class.getDeclaredField("objectMask");
//      sourceFile = Xml3GPP32435Parser.class.getDeclaredField("sourceFile");
//      measObjLdn = Xml3GPP32435Parser.class.getDeclaredField("measObjLdn");
//      oldObjClass = Xml3GPP32435Parser.class.getDeclaredField("oldObjClass");
//      suspectFlag = Xml3GPP32435Parser.class.getDeclaredField("suspectFlag");
//      measFile = Xml3GPP32435Parser.class.getDeclaredField("measFile");
//            
//      sourceFileC = SourceFile.class.getDeclaredConstructor(new Class[] { File.class, Properties.class,
//          RockFactory.class, RockFactory.class, ParseSession.class, ParserDebugger.class, Logger.class });
//      MeasurementFileImplC = MeasurementFileImpl.class.getDeclaredConstructor(new Class[] { SourceFile.class,
//          String.class, String.class, String.class, String.class, Logger.class });
//      ParseSessionC = ParseSession.class.getDeclaredConstructor(new Class[] {long.class, Properties.class});
//      
//      getSeconds.setAccessible(true);
//      handleTAGmoid.setAccessible(true);
//      calculateBegintime.setAccessible(true);
//      mainParserObject.setAccessible(true);
//      techPack.setAccessible(true);
//      setType.setAccessible(true);
//      setName.setAccessible(true);
//      status.setAccessible(true);
//      workerName.setAccessible(true);
//      charValue.setAccessible(true);
//      errorList.setAccessible(true);
//      fileFormatVersion.setAccessible(true);
//      vendorName.setAccessible(true);
//      dnPrefix.setAccessible(true);
//      fsLocalDN.setAccessible(true);
//      elementType.setAccessible(true);
//      collectionBeginTime.setAccessible(true);
//      measNameMap.setAccessible(true);
//      clusterMap.setAccessible(true);
//      origMeasNameMap.setAccessible(true);
//      meLocalDN.setAccessible(true);
//      userLabel.setAccessible(true);
//      swVersion.setAccessible(true);
//      measInfoId.setAccessible(true);
//      jobId.setAccessible(true);
//      granularityPeriodDuration.setAccessible(true);
//      granularityPeriodEndTime.setAccessible(true);
//      repPeriodDuration.setAccessible(true);
//      measIndex.setAccessible(true);
//      measValueIndex.setAccessible(true);
//      objectClass.setAccessible(true);
//      readVendorIDFrom.setAccessible(true);
//      fillEmptyMoidStyle.setAccessible(true);
//      fillEmptyMoidValue.setAccessible(true);
//      objectMask.setAccessible(true);
//      sourceFile.setAccessible(true);
//      measObjLdn.setAccessible(true);
//      oldObjClass.setAccessible(true);
//      suspectFlag.setAccessible(true);
//      measFile.setAccessible(true);
//      
//      sourceFileC.setAccessible(true);
//      MeasurementFileImplC.setAccessible(true);
//      ParseSessionC.setAccessible(true);
//      
//      //MeasurementFileImpl
//      data = MeasurementFileImpl.class.getDeclaredField("data");
//      
//      data.setAccessible(true);
//      
//      // Main
//      fileList = Main.class.getDeclaredField("fileList");
//      psession = Main.class.getDeclaredField("psession");
//      
//      fileList.setAccessible(true);
//      psession.setAccessible(true);
//      
//      
////      Field dMap = DataFormatCache.class.getDeclaredField("map");
////      dMap.setAccessible(true);
//
//      
//      ArrayList<DItem> al = new ArrayList();
//      DItem di1 = new DItem("DATETIME_ID", 1, "first",  "pi");
//      DItem di2 = new DItem("filename",    2, "second", "pi");
//      DItem di3 = new DItem("DIRNAME",     3, "third",  "pi");
//      al.add(di1);
//      al.add(di2);
//      al.add(di3);
//      
//      ArrayList<DItem> al2 = new ArrayList();
//      DItem di21 = new DItem("value", 1, "first", "pi");
//      al2.add(di21);
//      
//
//      DFormat df = new DFormat("tfid1", "foldName", "foldName","foldName", "foldName");
//      df.setItems(al);
//
//      DFormat df2 = new DFormat("tfid2", "foldName", "foldName","foldName", "foldName");
//      df2.setItems(al2);
//
//
//      final Map<String, DFormat> itmap = new HashMap<String, DFormat>();
//      itmap.put("tfid1:foldName", df);
//      itmap.put("tfid2:foldName", df2);
//
//
//      DataFormatCache.testInitialize(null, null, null, null);
////      HashMap hm = (HashMap) dMap.get(dfc);
////      hm.put("if:tagID", df);
////      hm.put("if2:tagID2", df2);
//
//      StaticProperties.giveProperties(new Properties());
//      
//    } catch (Exception e) {
//      e.printStackTrace();
//      fail("init() failed");
//    }
//    
//  }

	@Test
	public void testInit() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, "techPack", "setType", "setName", "workerName");

		try {

			String expected = "null,techPack,setType,setName,1,workerName";
			xml3gpp.run();
			String actual = "null" + "," + techPack.get(xml3gpp) + "," + setType.get(xml3gpp) + ","
					+ setName.get(xml3gpp) + "," + status.get(xml3gpp) + "," + workerName.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStatus() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		assertEquals(0, xml3gpp.status());
	}

	@Test
	public void testErrors() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		ArrayList al = new ArrayList();
		al.add("text");

		try {
			List list = xml3gpp.errors();

			assertEquals(false, list.contains("text"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//  @Test
//  public void testRun() {
//    Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
//    String homeDir = System.getProperty("user.home");
//    Properties sprop = new Properties();
//    sprop.setProperty("SessionHandling.storageFile", homeDir + File.separator + "storageFile");
//    sprop.setProperty("SessionHandling.log.types", "AdapterLog");
//    sprop.setProperty("SessionHandling.log.AdapterLog.class", "com.distocraft.dc5000.common.AdapterLog");
//    sprop.setProperty("SessionHandling.log.ADAPTER.inputTableDir", homeDir);
//    Properties prop = new Properties();
//    prop.setProperty("baseDir", homeDir);
//    prop.setProperty("interfaceName", "if2");
//    prop.setProperty("x3GPPParser.vendorIDMask", "f.+(tagID2)");
//    
//    /* Creating file to be parsed */
//    HelpClass hc = new HelpClass();
//    File x = hc.createFile(homeDir, "3GPPfile.xml", "<measValue measObjLdn=\"ffftagID2\">\n\t<measData>\n\t\t<measType p=\"key\">value</measType>\n" +
//                    "\t\t<r p=\"key\">rvalue</r>\n\t</measData>\n</measValue>");
//
//    x.deleteOnExit();
//    File out = new File(homeDir, "out");
//    out.mkdir();
//    
//    /* Initializing transformer cache */
//    TransformerCache tc = new TransformerCache();
//    
//    try {
//      MeasurementFileImpl.setTestMode(true);
//      ParseSession ps = (ParseSession) ParseSessionC.newInstance(new Object[] {1L, null});
//      Main main = new Main(prop, "x", "x", "x", null, null, new EngineCom());
//      Method createFileInformation = Main.class.getDeclaredMethod("createFileInformation", File.class, long.class, int.class);
//      createFileInformation.setAccessible(true);
//
//      ArrayList<Main.FileInformation> testFileList = new ArrayList<Main.FileInformation>();
//      testFileList.add((Main.FileInformation) createFileInformation.invoke(main, x, 0, 0));
//
//      psession.set(main, ps);
//      
//      /* Initializing Static Properties in order to initialize SessionHandler */
//      StaticProperties.giveProperties(sprop);
//      
//      /* Initializing SessionHandler */
//      SessionHandler.init();
//      
//      xml3gpp.init(main, "tp", "st", "sn", "wn");
//      
//      /* Calling the tested method */
//      xml3gpp.run();
//      
//      File tp = new File(homeDir + File.separator + "out\\tp");
//      File i = new File(homeDir + File.separator + "out\\tp\\foldName_wn_null");
//
//      String actual = hc.readFileToString(i);
//      String expected = "rvalue\t";
//
//      MeasurementFileImpl mf = (MeasurementFileImpl) measFile.get(xml3gpp);
//      mf.close(); // Should this be in endElement method
//
//      i.delete();
//      tp.delete();
//      out.delete();
//
//      assertEquals(expected, actual);
//      
//    } catch(Exception e) {
//      e.printStackTrace();
//      fail("testRun() failed");
//    }
//  }

//  @Test
//  public void testParseSourceFileStringStringString() {
//    Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
//    xml3gpp.init(null, "techpack", "st", "sn", "worker");
//    
//    String homeDir = System.getProperty("user.home");
//    
//    Properties prop = new Properties();
//    prop.setProperty("interfaceName", "if2");
//    prop.setProperty("baseDir", homeDir);
//    prop.setProperty("x3GPPParser.vendorIDMask", "f.+(tagID2)");
//    
//    HelpClass hc = new HelpClass();
//    File x = hc.createFile(homeDir, "3GPPfile.xml", "<measValue measObjLdn=\"ffftagID2\">\n\t<measData>\n\t\t<measType p=\"key\">value</measType>\n" +
//    		"\t\t<r p=\"key\">rvalue</r>\n\t</measData>\n</measValue>");
//    x.deleteOnExit();
//    File out = new File(homeDir, "out");
//    out.mkdir();
//    
//    /* Initializing transformer cache */
//    TransformerCache tc = new TransformerCache();
//    
//    try {
//      MeasurementFileImpl.setTestMode(true);
//      SourceFile sf = (SourceFile) sourceFileC.newInstance(new Object[] { x, prop, null, null, null, null, null });
//      
//      xml3gpp.parse(sf, "techPack", "setType", "setName");
//      
//      File tp = new File(homeDir + File.separator + "out\\techpack");
//      File i = new File(homeDir + File.separator + "out\\techpack\\foldName_worker_null");
//
//      String actual = hc.readFileToString(i);
//      String expected = "rvalue\t";
//
//      MeasurementFileImpl mf = (MeasurementFileImpl) measFile.get(xml3gpp);
//      mf.close(); // Should this be in endElement method
//
//      i.delete();
//      tp.delete();
//      out.delete();
//
//      assertEquals(expected, actual);
//      
//    } catch (Exception e) {
//      e.printStackTrace();
//      fail("testParseSourceFileStringStringString() failed");
//    }
//  }

	@Test
	public void testStrToMap() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		/* Calling the tested method */
		HashMap hm = xml3gpp.strToMap("this is string"); // ??? index++; ???

		assertTrue(hm.containsValue("this"));
	}

	@Test
	public void testStartElementStringStringStringAttributes1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "fileFormatVersion", "fileFormatVersion", "type", "ffv");
		atts.addAttribute("uri", "vendorName", "vendorName", "type", "VName");
		atts.addAttribute("uri", "dnPrefix", "dnPrefix", "type", "dnp");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "fileHeader", atts);

			String actual = "";
			String expected = "ffv,VName,dnp";
			actual += fileFormatVersion.get(xml3gpp) + "," + vendorName.get(xml3gpp) + "," + dnPrefix.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "localDn", "localDn", "type", "ldn");
		atts.addAttribute("uri", "elementType", "elementType", "type", "etype");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "fileSender", atts);

			String actual = "";
			String expected = "ldn,etype";
			actual += fsLocalDN.get(xml3gpp) + "," + elementType.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes3() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "beginTime", "beginTime", "type", "btime");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "measCollec", atts);

			String actual = "";
			String expected = "btime";
			actual += collectionBeginTime.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes4() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "beginTime", "beginTime", "type", "btime");

		try {
			HashMap hm = null;

			/* Calling the tested method */
			xml3gpp.startElement(null, null, "measData", atts);

			hm = (HashMap) measNameMap.get(xml3gpp);

			assertNotNull(hm);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes5() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "localDn", "localDn", "type", "ldn");
		atts.addAttribute("uri", "userLabel", "userLabel", "type", "ulabel");
		atts.addAttribute("uri", "swVersion", "swVersion", "type", "swv");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "managedElement", atts);

			String actual = "";
			String expected = "ldn,ulabel,swv";
			actual += meLocalDN.get(xml3gpp) + "," + userLabel.get(xml3gpp) + "," + swVersion.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes6() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "measInfoId", "measInfoId", "type", "mii");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "measInfo", atts);

			String actual = "";
			String expected = "mii";
			actual += measInfoId.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes7() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "jobId", "jobId", "type", "jid");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "job", atts);

			String actual = "";
			String expected = "jid";
			actual += jobId.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes8() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "duration", "duration", "type", "xx200S");
		atts.addAttribute("uri", "endTime", "endTime", "type", "etime");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "granPeriod", atts);

			String actual = "";
			String expected = "200,etime";
			actual += granularityPeriodDuration.get(xml3gpp) + "," + granularityPeriodEndTime.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes9() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "duration", "duration", "type", "xx300S");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "repPeriod", atts);

			String actual = "";
			String expected = "300";
			actual += repPeriodDuration.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes10() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "p", "p", "type", "P");

		try {
			/* Calling the tested method */

			xml3gpp.startElement(null, null, "measType", atts);
			String actual = "";
			String expected = "P";
			actual += measIndex.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes11() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");
		Properties prop = new Properties();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "measObjLdn", "measObjLdn", "type", "filename");
		Class classObj1 = xml3gpp.getClass();
		try {
			Field readVendorIDFrom = classObj1.getDeclaredField("readVendorIDFrom");
			readVendorIDFrom.setAccessible(true);
			readVendorIDFrom.set(xml3gpp, "data");
			startElement = Xml3GPP32435Parser.class.getDeclaredMethod("startElement", String.class);
			startElement.setAccessible(true);
			startElement.invoke(xml3gpp,null, null, "measValue", atts);
			SourceFile sf = (SourceFile) sourceFileC
					.newInstance(new Object[] { null, prop, null, null, null, null, null });
			sourceFile.set(xml3gpp, sf);
			objectMask.set(xml3gpp, "f.+(name)");
			readVendorIDFrom.set(xml3gpp, "data");

			/* Calling the tested method */

			String actual = "";
			String expected = "filename,name";
			actual += measObjLdn.get(xml3gpp) + "," + oldObjClass.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes11_1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");
		Properties prop = new Properties();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "measObjLdn", "measObjLdn", "type", "filename");
			Class classObj1 = xml3gpp.getClass();
			try {
				Field readVendorIDFrom = classObj1.getDeclaredField("readVendorIDFrom");
				readVendorIDFrom.setAccessible(true);
				readVendorIDFrom.set(xml3gpp, "measInfoId");
				Field hashData = classObj1.getDeclaredField("hashData");
				hashData.setAccessible(true);
				hashData.set(xml3gpp, true);
				Field measInfoId = classObj1.getDeclaredField("measInfoId");
				measInfoId.setAccessible(true);
				measInfoId.set(xml3gpp, "");
				Field hasDynCounters = classObj1.getDeclaredField("hasDynCounters");
				hasDynCounters.setAccessible(true);
				hasDynCounters.set(xml3gpp, true);
				Field hasFlexCounters = classObj1.getDeclaredField("hasFlexCounters");
				hasFlexCounters.setAccessible(true);
				hasFlexCounters.set(xml3gpp, true);
				startElement = Xml3GPP32435Parser.class.getDeclaredMethod("startElement", String.class, String.class, String.class,Attributes.class);
				startElement.setAccessible(true);
				startElement.invoke(xml3gpp,null, null, "measValue", atts);

			/* Calling the tested method */

			String actual = "";
			String expected = "filename,name";
			actual += measObjLdn.get(xml3gpp) + "," + oldObjClass.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes11_1_1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");
		Properties prop = new Properties();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "measObjLdn", "measObjLdn", "type", "filename");
		Class classObj1 = xml3gpp.getClass();
		try {
			Field readVendorIDFrom = classObj1.getDeclaredField("readVendorIDFrom");
			readVendorIDFrom.setAccessible(true);
			readVendorIDFrom.set(xml3gpp, "measInfoId");
			Field hashData = classObj1.getDeclaredField("hashData");
			hashData.setAccessible(true);
			hashData.set(xml3gpp, true);
			Field measInfoId = classObj1.getDeclaredField("measInfoId");
			measInfoId.setAccessible(true);
			measInfoId.set(xml3gpp, "");
			Field hasDynCounters = classObj1.getDeclaredField("hasDynCounters");
			hasDynCounters.setAccessible(true);
			hasDynCounters.set(xml3gpp, true);
			startElement = Xml3GPP32435Parser.class.getDeclaredMethod("startElement", String.class, String.class, String.class,Attributes.class);
			startElement.setAccessible(true);
			startElement.invoke(xml3gpp,null, null, "measValue", atts);
			/* Calling the tested method */

			String actual = "";
			String expected = "filename,name";
			actual += measObjLdn.get(xml3gpp) + "," + oldObjClass.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes11_1_2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");
		Properties prop = new Properties();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "measObjLdn", "measObjLdn", "type", "filename");
			Class classObj1 = xml3gpp.getClass();
			try {
				Field readVendorIDFrom = classObj1.getDeclaredField("readVendorIDFrom");
				readVendorIDFrom.setAccessible(true);
				readVendorIDFrom.set(xml3gpp, "measInfoId");
				Field hashData = classObj1.getDeclaredField("hashData");
				hashData.setAccessible(true);
				hashData.set(xml3gpp, true);
				Field measInfoId = classObj1.getDeclaredField("measInfoId");
				measInfoId.setAccessible(true);
				measInfoId.set(xml3gpp, "");
				Field hasFlexVectorCounters = classObj1.getDeclaredField("hasFlexVectorCounters");
				hasFlexVectorCounters.setAccessible(true);
				hasFlexVectorCounters.set(xml3gpp, true);
				startElement = Xml3GPP32435Parser.class.getDeclaredMethod("startElement", String.class, String.class, String.class,Attributes.class);
				startElement.setAccessible(true);
				startElement.invoke(xml3gpp,null, null, "measValue", atts);

			/* Calling the tested method */

			String actual = "";
			String expected = "filename,name";
			actual += measObjLdn.get(xml3gpp) + "," + oldObjClass.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes12() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "p", "p", "type", "P");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "r", atts);

			String actual = "";
			String expected = "P";
			actual += measValueIndex.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes13() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "p", "p", "type", "P");

		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "suspect", atts);

			String actual = "";
			String expected = "P";
			actual += measValueIndex.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStartElementStringStringStringAttributes14() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute("uri", "p", "p", "type", "P");
 
		try {
			/* Calling the tested method */
			xml3gpp.startElement(null, null, "fileFooter", atts);

			String actual = "";
			String expected = "P";
			actual += measValueIndex.get(xml3gpp);

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testextractCounterName1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		Class classObj1 = xml3gpp.getClass();
		try {
			extractCounterName = Xml3GPP32435Parser.class.getDeclaredMethod("extractCounterName", String.class);
			extractCounterName.setAccessible(true);
			extractCounterName.invoke(xml3gpp, ".for#");
		}catch(Exception e) {
			e.getMessage();
		}

	}

	@Test
	public void testextractCounterName2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		Class classObj1 = xml3gpp.getClass();
		try {
			extractCounterName = Xml3GPP32435Parser.class.getDeclaredMethod("extractCounterName", String.class);
			extractCounterName.setAccessible(true);
			extractCounterName.invoke(xml3gpp, "#for.");
		}catch(Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testextractCounterName3() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		try {
		extractCounterName = Xml3GPP32435Parser.class.getDeclaredMethod("extractCounterName", String.class);
		extractCounterName.setAccessible(true);
		extractCounterName.invoke(xml3gpp,"for");
		}catch(Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testgetDataIDFromProcessInstructions() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		try {

			getDataIDFromProcessInstructions = Xml3GPP32435Parser.class.getDeclaredMethod("getDataIDFromProcessInstructions", String.class, String.class, String.class);
			getDataIDFromProcessInstructions.setAccessible(true);
			getDataIDFromProcessInstructions.invoke(xml3gpp,"interface", "obj", "key");

		} catch (Exception e) {
			e.getMessage();
		}

	}

	@Test
	public void testHandleTAGmoid() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");

		try {
			readVendorIDFrom.set(xml3gpp, "data");
			fillEmptyMoidStyle.set(xml3gpp, "static");
			fillEmptyMoidValue.set(xml3gpp, "filename");
			objectMask.set(xml3gpp, "f.+(name)");

			/* Calling the tested method */
			handleTAGmoid.invoke(xml3gpp, new Object[] { "" });

			assertEquals("name", objectClass.get(xml3gpp));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetSeconds() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		try {
			/* Calling the tested method */
			String s = (String) getSeconds.invoke(xml3gpp, new Object[] { "xx100S" });

			assertEquals("100", s);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEndElementStringStringString1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		try {
			charValue.set(xml3gpp, "this is text");

			xml3gpp.endElement(null, null, "measTypes");

			/* Calling the tested method */
			HashMap hm = (HashMap) measNameMap.get(xml3gpp);

			assertEquals("text", hm.get("0"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEndElementStringStringString2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		try {
			charValue.set(xml3gpp, "Type");
			measIndex.set(xml3gpp, "key");
			measNameMap.set(xml3gpp, new HashMap());
			clusterMap.set(xml3gpp, new HashMap());
			origMeasNameMap.set(xml3gpp, new HashMap());

			/* Calling the tested method */
			xml3gpp.endElement(null, null, "measType");

			HashMap hm = (HashMap) measNameMap.get(xml3gpp);

			assertEquals("Type", hm.get("key"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//  @Test
//  public void testEndElementStringStringString3() {
//    Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
//    xml3gpp.init(null, null, null, null, "workerName");
//    
//    String homeDir = System.getProperty("user.home");
//    
//    Properties prop = new Properties();
//    prop.setProperty("interfaceName", "if");
//    prop.setProperty("baseDir", homeDir);
//    prop.setProperty("debug", "true");
//    
//    HelpClass hc = new HelpClass();
//    
//    Logger log = Logger.getLogger("log");
//    
//    File out = new File(homeDir, "out");
//    out.mkdir();
//    
//    TransformerCache tc = new TransformerCache();
//    
//    try {
//      final Map<String, DFormat> it_map = new HashMap<String, DFormat>();
//      final DFormat if_tagID = new DFormat("if", "tagID", "tagID", "tagID", "tagID");
//      it_map.put("", if_tagID);
//
//      DataFormatCache.testInitialize(it_map, null, null, null);
//
//      MeasurementFileImpl.setTestMode(true);
//      
//      granularityPeriodEndTime.set(xml3gpp, "2008-09-29T11:10:15+0300");
//      granularityPeriodDuration.set(xml3gpp, "10");
//      SourceFile sf = (SourceFile) sourceFileC.newInstance(new Object[] { null, prop, null, null,
//          null, null, null });
//      MeasurementFileImpl mf = (MeasurementFileImpl) MeasurementFileImplC.newInstance(new Object[] { sf, "tagID", "tp",
//          null, null, log });
//      
//      measFile.set(xml3gpp, mf);
//      
//      /* Calling the tested method */
//      xml3gpp.endElement(null, null, "measValue");
//
//      File tp = new File(homeDir + File.separator + "out\\tp");
//      File i = new File(homeDir + File.separator + "out\\tp\\foldName__null");
//
//      String actual = hc.readFileToString(i);
//      String expected = "2008-09-29T11:10:05+0300\tdummyfile\tdummydir\t";
//
//      mf.close(); // Should this be in endElement method
//
//      i.delete();
//      tp.delete();
//      out.delete();
//
//      assertEquals(expected, actual);
//      
//    } catch (Exception e) {
//      e.printStackTrace();
//      fail("testEndElementStringStringString3()");
//    }
//  }

	@Test
	public void testEndElementStringStringString4() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");

		Properties prop = new Properties();
		prop.setProperty("interfacename", "if");

		HashMap hm = new HashMap();
		hm.put("0", "value");

		Logger log = Logger.getLogger("log");

		try {
			charValue.set(xml3gpp, "Type");
			measNameMap.set(xml3gpp, hm);
			SourceFile sf = (SourceFile) sourceFileC
					.newInstance(new Object[] { null, prop, null, null, null, null, null });
			MeasurementFileImpl mf = (MeasurementFileImpl) MeasurementFileImplC
					.newInstance(new Object[] { sf, "tagID", null, null, null, log });

			measFile.set(xml3gpp, mf);

			/* Calling the tested method */
			xml3gpp.endElement(null, null, "measResults");

			HashMap map = (HashMap) data.get(mf);

			assertEquals("Type", map.get("value"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEndElementStringStringString5() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");

		Properties prop = new Properties();
		prop.setProperty("interfacename", "if");

		HashMap hm = new HashMap();
		hm.put("key", "value");

		Logger log = Logger.getLogger("log");

		try {
			charValue.set(xml3gpp, "Type");
			measValueIndex.set(xml3gpp, "key");
			measNameMap.set(xml3gpp, hm);
			SourceFile sf = (SourceFile) sourceFileC
					.newInstance(new Object[] { null, prop, null, null, null, null, null });
			MeasurementFileImpl mf = (MeasurementFileImpl) MeasurementFileImplC
					.newInstance(new Object[] { sf, "tagID", null, null, null, log });

			measFile.set(xml3gpp, mf);

			/* Calling the tested method */
			xml3gpp.endElement(null, null, "r");

			HashMap map = (HashMap) data.get(mf);

			assertEquals("Type", map.get("value"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEndElementStringStringString6() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		try {
			charValue.set(xml3gpp, "flag");

			/* Calling the tested method */
			xml3gpp.endElement(null, null, "suspect");

			assertEquals("flag", suspectFlag.get(xml3gpp));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void voidCalculateBegintimeplus() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		Class classObj1 = xml3gpp.getClass();
		try {
				Field granularityPeriodEndTime = classObj1.getDeclaredField("granularityPeriodEndTime");
				granularityPeriodEndTime.setAccessible(true);
				granularityPeriodEndTime.set(xml3gpp, "2008-09-29T11:10:15+0300");

				Field granularityPeriodDuration = classObj1.getDeclaredField("granularityPeriodEndTime");
				granularityPeriodDuration.setAccessible(true);
				granularityPeriodDuration.set(xml3gpp,"600");
			/* Calling the tested method */
			String s = (String) calculateBegintime.invoke(xml3gpp, new Object[] {});

			assertEquals("2008-09-29T09:00:15+0100", s);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void voidCalculateBegintimeminus() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		Class classObj1 = xml3gpp.getClass();
		try {

			Field granularityPeriodEndTime = classObj1.getDeclaredField("granularityPeriodEndTime");
			granularityPeriodEndTime.setAccessible(true);
			granularityPeriodEndTime.set(xml3gpp, "2008-09-29T11:10:15-0300");

			Field granularityPeriodDuration = classObj1.getDeclaredField("granularityPeriodEndTime");
			granularityPeriodDuration.setAccessible(true);
			granularityPeriodDuration.set(xml3gpp, "10");
			

			/* Calling the tested method */
			String s = (String) calculateBegintime.invoke(xml3gpp, null);

			assertEquals("2008-09-29T11:10:05-0300", s);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testParseFileName1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		xml3gpp.init(null, null, null, null, "workerName");

		Class classObj1 = xml3gpp.getClass();
		try {
		Field field1 = classObj1.getDeclaredField("matchObjectMaskAgainst");
		field1.setAccessible(true);
		field1.set(xml3gpp, "whole");
		parseFileName = Xml3GPP32435Parser.class.getDeclaredMethod("parseFileName", String.class,String.class);
		parseFileName.setAccessible(true);
		String s = (String) parseFileName.invoke(xml3gpp, "filename", "f.+(name)");
		assertEquals("name", s);		

		parseFileName = Xml3GPP32435Parser.class.getDeclaredMethod("parseFileName", String.class,String.class);
		parseFileName.setAccessible(true);
		String s1 = (String) parseFileName.invoke(xml3gpp, "filename", "f.+(str)");

		assertEquals("", s1);

		
		Field field2 = classObj1.getDeclaredField("matchObjectMaskAgainst");
		field2.setAccessible(true);
		field2.set(xml3gpp, "wholeCookie");
		
		parseFileName = Xml3GPP32435Parser.class.getDeclaredMethod("parseFileName", String.class,String.class);
		parseFileName.setAccessible(true);
		String s2 = (String) parseFileName.invoke(xml3gpp, "filename", "f.+(name)");
		assertEquals("name", s2);

		parseFileName = Xml3GPP32435Parser.class.getDeclaredMethod("parseFileName", String.class,String.class);
		parseFileName.setAccessible(true);
		String s4 = (String) parseFileName.invoke(xml3gpp, "filename", "f.+(str)");
		assertEquals("", s4);
		}catch(Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testcharacters() {

		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		char[] ch = new char[] { 's', 'm', 'a', '\n' };
		try {
			xml3gpp.characters(ch, 0, 4);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testcalculateBegintime1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("granularityPeriodEndTime");
			field1.setAccessible(true);
			field1.set(xml3gpp, "2021-09-23'T'09:09:09Z");

			calculateBegintime = Xml3GPP32435Parser.class.getDeclaredMethod("calculateBegintime", null);
			calculateBegintime.setAccessible(true);
			calculateBegintime.invoke(xml3gpp, null);
		} catch (Exception e) {
			e.getMessage();
		}

	}

	@Test
	public void testcalculateBegintime2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("granularityPeriodEndTime");
			field1.setAccessible(true);
			field1.set(xml3gpp, ".+\\\\+\\\\d\\\\d(:)\\\\d\\\\d");

			calculateBegintime = Xml3GPP32435Parser.class.getDeclaredMethod("calculateBegintime", null);
			calculateBegintime.setAccessible(true);
			calculateBegintime.invoke(xml3gpp, null);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testhandlesFlexTrue() {

			Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
			Class classObj1 = xml3gpp.getClass();
			Map m = new HashMap();
			try {
				Field field1 = classObj1.getDeclaredField("flexFilterMap");
				field1.setAccessible(true);
				field1.set(xml3gpp, m);
				Field field2 = classObj1.getDeclaredField("charValue");
				field2.setAccessible(true);
				field2.set(xml3gpp, "Nil");
				handlesFlex = Xml3GPP32435Parser.class.getDeclaredMethod("handlesFlex",  boolean.class,String.class);
				handlesFlex.setAccessible(true);
				handlesFlex.invoke(xml3gpp, true, "(.+?)_(.*)");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testhandlesFlexFalse() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		Class classObj1 = xml3gpp.getClass();
		Map m = new HashMap();
		try {
			Field field1 = classObj1.getDeclaredField("flexFilterMap");
			field1.setAccessible(true);
			field1.set(xml3gpp, m);
			Field field2 = classObj1.getDeclaredField("charValue");
			field2.setAccessible(true);
			field2.set(xml3gpp, "Nil");
			handlesFlex = Xml3GPP32435Parser.class.getDeclaredMethod("handlesFlex",  boolean.class,String.class);
			handlesFlex.setAccessible(true);
			handlesFlex.invoke(xml3gpp, false, "(.+?)_(*)");
	} catch (Exception e) {
		e.getMessage();
	}
	}

	@Test
	public void testhandlesFlex1() {
			Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
			Class classObj1 = xml3gpp.getClass();
			Map m = new HashMap();
			m.put("obj1","val");
			try {
				Field field1 = classObj1.getDeclaredField("flexFilterMap");
				field1.setAccessible(true);
				field1.set(xml3gpp, m);
				Field field2 = classObj1.getDeclaredField("charValue");
				field2.setAccessible(true);
				field2.set(xml3gpp, "Nil");
				handlesFlex = Xml3GPP32435Parser.class.getDeclaredMethod("handlesFlex",  boolean.class,String.class);
				handlesFlex.setAccessible(true);
				handlesFlex.invoke(xml3gpp, true, "(.+?)_(*)");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testhandleDyn1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		Class classObj1 = xml3gpp.getClass();
		Map m = new HashMap();
		try {

			Field field1 = classObj1.getDeclaredField("charValue");
			field1.setAccessible(true);
			field1.set(xml3gpp, "Nil");
			handleDyn = Xml3GPP32435Parser.class.getDeclaredMethod("handleDyn",  String.class,String.class,String.class);
			handleDyn.setAccessible(true);
			handleDyn.invoke(xml3gpp, "", "0", "3");

		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testhandleDyn2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		Class classObj1 = xml3gpp.getClass();
		Map m = new HashMap();
		try {

			Field field1 = classObj1.getDeclaredField("charValue");
			field1.setAccessible(true);
			field1.set(xml3gpp, "dynamicData");
			m.put("1", "cal");
			m.put("obj2", "cal2");
			m.put("obj3","cal3");
			Field field5 = classObj1.getDeclaredField("dynIndexMap");
			field5.setAccessible(true);
			field5.set(xml3gpp, m);
			checkIfDyn = Xml3GPP32435Parser.class.getDeclaredMethod("handleDyn", String.class,String.class,String.class);
			checkIfDyn.setAccessible(true);
			checkIfDyn.invoke(xml3gpp, "Smart", "1", "3");
			//----------------------------------------------------------------------------------------------------

		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testcheckIfDyn() {

		try {

			Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
			Class classObj1 = xml3gpp.getClass();
			Field field1 = classObj1.getDeclaredField("dynMask");
			field1.setAccessible(true);
			field1.set(xml3gpp, "dynamicData");
			// xml3gpp.dynPartIndex = 0;
			Field field2 = classObj1.getDeclaredField("dynPartIndex");
			field2.setAccessible(true);
			field2.set(xml3gpp, 0);
			// xml3gpp.dynIndexIndex = 0;
			Field field3 = classObj1.getDeclaredField("dynIndexIndex");
			field3.setAccessible(true);
			field3.set(xml3gpp, 0);
			// xml3gpp.staticPartIndex = 0;
			Field field4 = classObj1.getDeclaredField("staticPartIndex");
			field4.setAccessible(true);
			field4.set(xml3gpp, 0);
			// xml3gpp.dynCounterBin = new HashMap();
			Map m = new HashMap();
			m.put("dynamicData", "dynamicData");
			Field field5 = classObj1.getDeclaredField("dynCounterBin");
			field5.setAccessible(true);
			field5.set(xml3gpp, m);

			checkIfDyn = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfDyn", String.class);
			checkIfDyn.setAccessible(true);
			String str = "dynamicData";
			checkIfDyn.invoke(xml3gpp, str);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testcalculateBegintime3() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("granularityPeriodEndTime");
			field1.setAccessible(true);
			field1.set(xml3gpp, "start");

			calculateBegintime = Xml3GPP32435Parser.class.getDeclaredMethod("calculateBegintime", null);
			calculateBegintime.setAccessible(true);
			Boolean val1 = (Boolean) calculateBegintime.invoke(xml3gpp, null);
		} catch (Exception e) {
			e.getMessage();
		}

	}

	@Test
	public void testgetSeconds1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		String str = "NameSort";
		try {
			getSeconds = Xml3GPP32435Parser.class.getDeclaredMethod("getSeconds", String.class);
			getSeconds.setAccessible(true);
			getSeconds.invoke(xml3gpp, str);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testgetSeconds2() {

		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();

		String str = "Po23Man";
		try {
			getSeconds = Xml3GPP32435Parser.class.getDeclaredMethod("getSeconds", String.class);
			getSeconds.setAccessible(true);
			getSeconds.invoke(xml3gpp, str);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@AfterClass
	public static void clean() {
		File i = new File(System.getProperty("user.home"), "storageFile");
		i.deleteOnExit();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		objectUnderTest = new Xml3GPP32435Parser();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDecompressVector101() throws Exception {
		List input = makeArrayList("1,0,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("1");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVector20112() throws Exception {
		List input = makeArrayList("2,0,1,1,2");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("1,2");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVector123() throws Exception {
		List input = makeArrayList("1,2,3");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("3");
		assertEquals(result, expected);
	}

	@Test
	public void testDecompressVector20123() throws Exception {
		List input = makeArrayList("2,0,1,2,3");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("1,  3");
	}

	@Test
	public void testDecompressVector22301() throws Exception {
		List input = makeArrayList("2,2,3,0,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("3,1");
		assertEquals(result, expected);
	}

	@Test
	public void testDecompressVector20120() throws Exception {
		List input = makeArrayList("2,0,1,2,0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("1,0");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVectorEmptyString() throws Exception {
		List input = new ArrayList();
		input.add("");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(result.equals(input));
	}

	@Test
	public void testDecompressVector20empty20() throws Exception {
		List input = new ArrayList();
		input.add("2");
		input.add("0");
		input.add("");
		input.add("2");
		input.add("0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = new ArrayList();
		expected.add("");
		expected.add("0");
		assertTrue(result.equals(expected));
	}

	// EMPTY DATA:

	@Test
	public void testDecompressVector0() throws Exception {
		List input = makeArrayList("0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("0");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVector100() throws Exception {
		List input = makeArrayList("1,0,0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("0");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVector110() throws Exception {
		List input = makeArrayList("1,1,0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("0");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVector130() throws Exception {
		List input = makeArrayList("1,3,0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("0");
		assertTrue(result.equals(expected));
	}

	Set<String> rangeCountersList = new HashSet<>();
	Set<String> cmVectorCounters = new HashSet<>();
	Set<String> flexRangeCounters = new HashSet<>();
	Set<String> keyColumns = new HashSet<>();

	@Test
	public void testcheckIfFlexVector1() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsMacRBSymUtilDlCaSCellPartitionDistr");
		flexRangeCounters.add("pmEbsMacUeThpUlMbbLowVolDistr");
		flexRangeCounters.add("pmEbsMacLatTimeDlDrxSyncQos");
		flexRangeCounters.add("pmEbsMacVolDlDrbQos");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("rangeCounters", rangeCountersList);
		map.put("cmVectorCounters", cmVectorCounters);
		map.put("flexRangeCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("vectorMeasurement");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);

			checkIfFlexVector = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlexVector", String.class);
			checkIfFlexVector.setAccessible(true);
			String str = "pmEbsMacVolDlDrbQos";
			Boolean val1 = (Boolean) checkIfFlexVector.invoke(xml3gpp, str);
			assertTrue(val1);
			// Boolean val1 = xml3gpp.checkIfFlexVector("pmEbsMacVolDlDrbQos");
			// assertTrue(val1);
		} catch (Exception e) {
			e.getMessage(); 
		}
	}

	@Test
	public void testcheckIfFlexVector2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsMacRBSymUtilDlCaSCellPartitionDistr");
		flexRangeCounters.add("pmEbsMacUeThpUlMbbLowVolDistr");
		flexRangeCounters.add("pmEbsMacLatTimeDlDrxSyncQos");
		flexRangeCounters.add("pmEbsMacVolDlDrbQos");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("rangeCounters", rangeCountersList);
		map.put("cmVectorCounters", cmVectorCounters);
		map.put("flexRangeCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("vectorMeasurement");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);

			checkIfFlexVector = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlexVector", String.class);
			checkIfFlexVector.setAccessible(true);
			String str = "pmEbsMacVolDlDrbQos_flexCounter";
			Boolean val1 = (Boolean) checkIfFlexVector.invoke(xml3gpp, str);
			assertTrue(val1);
			// Boolean val2 = xml3gpp.checkIfFlexVector("pmEbsMacVolDlDrbQos_flexCounter");
			// assertTrue(val2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testcheckIfFlexVector3() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsMacRBSymUtilDlCaSCellPartitionDistr");
		flexRangeCounters.add("pmEbsMacUeThpUlMbbLowVolDistr");
		flexRangeCounters.add("pmEbsMacLatTimeDlDrxSyncQos");
		flexRangeCounters.add("pmEbsMacVolDlDrbQos");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("rangeCounters", rangeCountersList);
		map.put("cmVectorCounters", cmVectorCounters);
		map.put("flexRangeCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("vectorMeasurement");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);

			checkIfFlexVector = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlexVector", String.class);
			checkIfFlexVector.setAccessible(true);
			String str = "flexCounter";
			Boolean val1 = (Boolean) checkIfFlexVector.invoke(xml3gpp, str);
			assertFalse(val1);
			//// Boolean val3 = xml3gpp.checkIfFlexVector("flexCounter");
			// assertFalse(val3);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testcheckIfFlexVector4() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsMacRBSymUtilDlCaSCellPartitionDistr");
		flexRangeCounters.add("pmEbsMacUeThpUlMbbLowVolDistr");
		flexRangeCounters.add("pmEbsMacLatTimeDlDrxSyncQos");
		flexRangeCounters.add("pmEbsMacVolDlDrbQos");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		// xml3gpp.vectorMeasurement
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("rangeCounters", rangeCountersList);
		map.put("cmVectorCounters", cmVectorCounters);
		map.put("flexRangeCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("vectorMeasurement");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);

			checkIfFlexVector = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlexVector", String.class);
			checkIfFlexVector.setAccessible(true);
			String str = "flex_Counter";
			Boolean val1 = (Boolean) checkIfFlexVector.invoke(xml3gpp, str);
			assertFalse(val1);
			// Boolean val4 = xml3gpp.checkIfFlexVector("flex_Counter");
			// assertFalse(val4);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void testcheckIfFlex1() {

		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsActiveUeDlMax");
		flexRangeCounters.add("pmEbsUeExclThpUlSmallVol");
		flexRangeCounters.add("pmEbsRrcConnReestSucc");
		flexRangeCounters.add("pmEbsRrcConnReestAtt");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("flexCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("flexCounterBin");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);
			checkIfFlex = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlex", String.class);
			checkIfFlex.setAccessible(true);
			String str = "pmEbsActiveUeDlMax_flex";
			Boolean val1 = (Boolean) checkIfFlex.invoke(xml3gpp, str);
			assertTrue(val1);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testcheckIfFlex2() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsActiveUeDlMax");
		flexRangeCounters.add("pmEbsUeExclThpUlSmallVol");
		flexRangeCounters.add("pmEbsRrcConnReestSucc");
		flexRangeCounters.add("pmEbsRrcConnReestAtt");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("flexCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("flexCounterBin");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);

			checkIfFlex = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlex", String.class);
			Parameter[] param = checkIfFlex.getParameters();
			System.out.println("----------------///////-------" + param[0]);

			checkIfFlex.setAccessible(true);
			String str = "pmEbsUeExclThpUlSmallVol";
			Boolean val1 = (Boolean) checkIfFlex.invoke(xml3gpp, str);
			;
			assertTrue(val1);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testcheckIfFlex3() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsActiveUeDlMax");
		flexRangeCounters.add("pmEbsUeExclThpUlSmallVol");
		flexRangeCounters.add("pmEbsRrcConnReestSucc");
		flexRangeCounters.add("pmEbsRrcConnReestAtt");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("flexCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("flexCounterBin");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);

			checkIfFlex = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlex", String.class);
			Parameter[] param = checkIfFlex.getParameters();
			System.out.println("----------------///////-------" + param[0]);

			checkIfFlex.setAccessible(true);
			String str = "counter";
			Boolean val1 = (Boolean) checkIfFlex.invoke(xml3gpp, str);
			;
			assertFalse(val1);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testcheckIfFlex4() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		flexRangeCounters.add("pmEbsActiveUeDlMax");
		flexRangeCounters.add("pmEbsUeExclThpUlSmallVol");
		flexRangeCounters.add("pmEbsRrcConnReestSucc");
		flexRangeCounters.add("pmEbsRrcConnReestAtt");
		keyColumns.add("FLEX_FILTERNAME");
		keyColumns.add("NR_NAME");
		keyColumns.add("MOID.NRCellDU");
		keyColumns.add("MOID");
		Map map = new HashMap();
		map.put("filename",
				"A20220614.0100+0200-0115+0200_SubNetwork=ONRM_ROOT_MO,SubNetwork=G2RBS,MeContext=G2RBS01_osscounterfile_DU_1");
		map.put("flexCounters", flexRangeCounters);
		map.put("keyColumns", keyColumns);
		Class classObj1 = xml3gpp.getClass();
		try {
			Field field1 = classObj1.getDeclaredField("flexCounterBin");
			field1.setAccessible(true);
			field1.set(xml3gpp, map);
			checkIfFlex = Xml3GPP32435Parser.class.getDeclaredMethod("checkIfFlex", String.class);
			Parameter[] param = checkIfFlex.getParameters();
			checkIfFlex.setAccessible(true);
			String str = "flex_counter";
			Boolean val1 = (Boolean) checkIfFlex.invoke(xml3gpp, str);
			assertFalse(val1);
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@Test
	public void testDecompressVector20010() throws Exception {
		List input = makeArrayList("2,0,0,1,0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("0,0");
		assertTrue(result.equals(expected));
	}

	@Test
	public void testDecompressVector20020() throws Exception {
		List input = makeArrayList("2,0,0,2,0");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		List expected = makeArrayList("0,0");
		assertTrue(result.equals(expected));
	}

	// BAD KEY:

	@Test
	public void testDecompressVectorKeyHasBadChar() throws Exception {
		List input = makeArrayList("'-1,0,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVectorKeyNegative() throws Exception {
		List input = makeArrayList("-1,0,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVectorKeyNotWholeNum() throws Exception {
		List input = makeArrayList("1.5,0,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector201() throws Exception {
		List input = makeArrayList("2,0,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector10123() throws Exception {
		List input = makeArrayList("1,0,1,2,3");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector201238() throws Exception {
		List input = makeArrayList("2,0,1,2,3,8");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector01235() throws Exception {
		List input = makeArrayList("0,1,2,3,5");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	// BAD INDEX:

	@Test
	public void testDecompressVector11dot52() throws Exception {
		List input = makeArrayList("1,1.5,2");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector1minus12() throws Exception {
		List input = makeArrayList("1,-1,2");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector210minus12() throws Exception {
		List input = makeArrayList("2,1,0,-1,2");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector1minus1dot51() throws Exception {
		List input = makeArrayList("1,-1.5,1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector1() throws Exception {
		List input = makeArrayList("1");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector12() throws Exception {
		List input = makeArrayList("1,2");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testDecompressVector1234() throws Exception {
		List input = makeArrayList("1,2,3,4");
		List result = objectUnderTest.getvaluesfromcompressVector(input);
		assertTrue(null == result);
	}

	@Test
	public void testgetMatcher() throws Exception {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		try {
			getMatcher = Xml3GPP32435Parser.class.getDeclaredMethod("getMatcher", String.class,String.class);
			getMatcher.setAccessible(true);
			getMatcher.invoke(xml3gpp, "String", "aa");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testendDocumentNull() {
		Xml3GPP32435Parser xml3gpp = new Xml3GPP32435Parser();
		try {
			endDocument = Xml3GPP32435Parser.class.getDeclaredMethod("endDocument", new Class[] {});
			endDocument.invoke(xml3gpp, new Method[] {});
			// obj.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param input expected to be a String of comma separated entities
	 * @return and ArrayList object whose elements are the entities listed in input
	 *         - entities returned are of type String - in same order as in input.
	 */
	private List makeArrayList(String input) {
		StringTokenizer bins = new StringTokenizer(input, ",");
		List inputArrayList = new ArrayList();
		while (bins.hasMoreElements()) {
			inputArrayList.add(bins.nextToken());
		}
		return inputArrayList;
	}

}
