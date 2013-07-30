package net.sf.odinms.exttools.wzextract;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import net.sf.odinms.provider.MapleData;
import net.sf.odinms.provider.MapleDataDirectoryEntry;
import net.sf.odinms.provider.MapleDataEntry;
import net.sf.odinms.provider.MapleDataProvider;
import net.sf.odinms.provider.MapleDataProviderFactory;

public class XMLWzExtract {
	public void extractWZ(String wzName, File outputBaseDirectory) {
		if (!outputBaseDirectory.exists()) {
			outputBaseDirectory.mkdir();
		} else {
			//System.out.println("skipping " + wzName + " (directory exists)");
		}
		MapleDataProvider dataProv = MapleDataProviderFactory.getDataProvider(new File(wzName));
		MapleDataDirectoryEntry root = dataProv.getRoot();
		
		dumpDirectory(dataProv, "", root, outputBaseDirectory);
	}

	private void dumpDirectory(MapleDataProvider dataProv, String path, MapleDataDirectoryEntry root,
								File outputBaseDirectory) {
		File file = new File(outputBaseDirectory, root.getName());
		file.mkdir();
		for (MapleDataEntry entry : root.getFiles()) {
			File xmlOutFile = new File(file, entry.getName() + ".xml");
			try {
				String filePath = path;
				if (filePath.length() > 0) {
					filePath += "/";
				}
				filePath += entry.getName();
				xmlOutFile.createNewFile();
				System.out.println("Dumping file " + filePath + " from " + dataProv.getRoot().getName() + " to " +
					xmlOutFile.getPath());
				dumpImg(dataProv.getData(filePath), new FileOutputStream(xmlOutFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (MapleDataDirectoryEntry child : root.getSubdirectories()) {
			dumpDirectory(dataProv, path + (path.equals("") ? "" : "/") + child.getName(), child, file);
		}
	}

	private void dumpImg(MapleData wzFile, OutputStream os) {
		PrintWriter pw = new PrintWriter(os);
		pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		dumpData(wzFile, pw, 0);
		pw.flush();
	}

	private void dumpData(MapleData data, PrintWriter pw, int level) {
		switch (data.getType()) {
			case PROPERTY:
				pw.println(indentation(level) + openNamedTag("imgdir", data.getName(), true));
				dumpDataList(data.getChildren(), pw, level + 1);
				pw.println(indentation(level) + closeTag("imgdir"));
				break;
			case CANVAS:
				pw.println(indentation(level) + openNamedTag("canvas", data.getName(), true));
				dumpDataList(data.getChildren(), pw, level + 1);
				pw.println(indentation(level) + closeTag("canvas"));
				break;
			case CONVEX:
				pw.println(indentation(level) + openNamedTag("convex", data.getName(), true));
				dumpDataList(data.getChildren(), pw, level + 1);
				pw.println(indentation(level) + closeTag("convex"));
				break;
			case SOUND:
				pw.println(indentation(level) + emptyNamedTag("sound", data.getName()));
				break;
			case UOL:
				pw.println(indentation(level) + emptyNamedValuePair("uol", data.getName(), data.getData().toString()));
				break;
			case DOUBLE:
				pw.println(indentation(level) +
					emptyNamedValuePair("double", data.getName(), data.getData().toString()));
				break;
			case FLOAT:
				pw.println(indentation(level) + emptyNamedValuePair("float", data.getName(), data.getData().toString()));
				break;
			case INT:
				pw.println(indentation(level) + emptyNamedValuePair("int", data.getName(), data.getData().toString()));
				break;
			case SHORT:
				pw.println(indentation(level) + emptyNamedValuePair("short", data.getName(), data.getData().toString()));
				break;
			case STRING:
				pw.println(indentation(level) +
					emptyNamedValuePair("string", data.getName(), data.getData().toString()));
				break;
			case VECTOR:
				Point tPoint = (Point) data.getData();
				pw.println(indentation(level) + openNamedTag("vector", data.getName(), false, false) +
					attrib("x", Integer.toString(tPoint.x)) + attrib("y", Integer.toString(tPoint.y), true, true));
				break;
			case IMG_0x00:
				pw.println(indentation(level) + emptyNamedTag("null", data.getName()));
				break;
			default:
				throw new RuntimeException("Unexpected img data type");
		}
	}

	private void dumpDataList(List<MapleData> datalist, PrintWriter pw, int level) {
		for (MapleData data : datalist) {
			dumpData(data, pw, level);
		}
	}

	private String openNamedTag(String tag, String name, boolean finish) {
		return openNamedTag(tag, name, finish, false);
	}

	private String emptyNamedTag(String tag, String name) {
		return openNamedTag(tag, name, true, true);
	}

	private String emptyNamedValuePair(String tag, String name, String value) {
		return openNamedTag(tag, name, false, false) + attrib("value", value, true, true);
	}

	private String openNamedTag(String tag, String name, boolean finish, boolean empty) {
		return "<" + tag + " name=\"" + name + "\"" + (finish ? (empty ? "/>" : ">") : " ");
	}

	private String attrib(String name, String value) {
		return attrib(name, XmlUtil.sanitizeText(value), false, false);
	}

	private String attrib(String name, String value, boolean closeTag, boolean empty) {
		return name + "=\"" + XmlUtil.sanitizeText(value) + "\"" + (closeTag ? (empty ? "/>" : ">") : " ");
	}

	private String closeTag(String tag) {
		return "</" + tag + ">";
	}

	private String indentation(int level) {
		char[] indent = new char[level];
		for (int i = 0; i < indent.length; i++) {
			indent[i] = '\t';
		}
		return new String(indent);
	}

	public static void main(String args[]) {
		for (String file : args) {
			try {
				XMLWzExtract wzExtract = new XMLWzExtract();
				wzExtract.extractWZ(file, new File("xmlout"));
			} catch (Exception e) {
				System.out.println("Exception occured while dumping " + file + " continuing with next file");
				e.printStackTrace();
			}
		}
	}
}
