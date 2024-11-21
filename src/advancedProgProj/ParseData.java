package advancedProgProj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParseData 
{
	private String sampleID;
	private List<String> bactNames;
	private String vitalStatus;
	
	public String getSubjectID(){
		return sampleID;
	}
	
	public List<String> getBactNames(){
		return bactNames;
	}
	
	public String getVitalStatus(){
		return vitalStatus;
	}
	
	@Override
	public String toString(){
		return "SampleID: " + sampleID + "\nVital Status: " + vitalStatus + "\nBacteria Found: " + bactNames;
	}	
	
	public ParseData()
	{
		
		
	}
	
	public static String generateSampleID(String line)
	{
		String[] splits = line.split(",");
		if (splits.length < 1) {
			throw new IllegalArgumentException("Line is too short to extract SampleID: " + line);
		}
		return splits[0].substring(1,splits[0].length()-1);
	}
	
	public static String generateVitalStatus(String line)
	{
		String[] splits = line.split(",");
		if (splits.length < 13) {
			throw new IllegalArgumentException("Line is too short to extract Vital Status: " + line);
		}
		return splits[12].substring(1,splits[12].length()-1);
	}
	
	public static List<String> generateHeaders(String filePath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		List<String> headers = new ArrayList<>();
		String s = reader.readLine();
		String[] x = s.split(",");
		for(int i = 0; i < x.length; i++)
		{
			headers.add(x[i].substring(1,x[i].length()-1));
		}
		return headers;
	}
	
	public static List<String> generateBacteriaNames(String line, List<String> headers)
	{
		List<String> nonZero = new ArrayList<>();
		String[] splits = line.split(",");
		for (int i = 1; i < splits.length; i++)
		{
			try
			{
				if(i >= headers.size()) {
					System.out.println("Skipping column due to missing headers for index " + i);
					continue;
				}
				String valueStr = splits[i].trim();
				valueStr = valueStr.replaceAll("[^0-9.-]", "");
				if (!valueStr.isEmpty())
				{
					int value = Integer.parseInt(valueStr);
					if (value > 0)
					{
						nonZero.add(headers.get(i));
					}
				}
				
			} catch (NumberFormatException e)
			{
				System.out.println("Invalid number format for values: " + splits[i]);
			}
		}
		return nonZero;
	}
	
	//Static factory method to build objects with correct information
	public static HashMap<String, String> parseMetaFile(String filePath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		HashMap<String, String> map = new HashMap<String, String>();
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			try
			{
				String id = generateSampleID(s);
				String vital = generateVitalStatus(s);
				map.put(id, vital);
			} 
			catch (IllegalArgumentException e) 
			{
				System.out.println("Skipping invalid data: " + e.getMessage());
			}
		}
		reader.close();
		return map;
	}
	
	public static HashMap<String, List<String>> parseReadFile(String filePath, List<String> headers) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		HashMap<String, List<String>> map = new HashMap<>();
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			try
			{
				if (s.trim().isEmpty()) {
					continue;
				}
				String[] parts = s.split(",");
				if(parts.length < headers.size()) {
					System.out.println("Skipping line due to insufficient cols: " + s);
					continue;
				}
				String id = generateSampleID(s);
				List<String> names = generateBacteriaNames(s, headers);
				//System.out.println(id+": " + names);
				if (id != null && names != null) {
					map.put(id, names);
				} else {
					System.out.println("Skipping line due to invalid data");
				}
			}
			catch (IllegalArgumentException e)
			{
				System.out.println("Skipping invalid data: " + e.getMessage());
			}
		}
		reader.close();
		//map.forEach((key, value) -> System.out.println(key + ": " + value));
		return map;
	}
	
	public static String[][] combineData(HashMap<String, String> metaData, HashMap<String, List<String>> readData)
	{
		int rowCount = metaData.size();
		String[][] combinedData = new String[rowCount][3];
		
		int index = 0;
		
		for (Map.Entry<String, String> entry : metaData.entrySet())
		{
			String sampleID = entry.getKey();
			String vitalStatus = entry.getValue();
			//System.out.println(sampleID + ": " + vitalStatus);
			
			List<String> bactNames = readData.get(sampleID);
			//System.out.println(bactNames);
			if(bactNames != null)
			{
				String bactNamesStr = String.join(",", bactNames);
				//System.out.println(bactNamesStr);
				combinedData[index] = new String[] {sampleID, vitalStatus, bactNamesStr};
				index++;
			} else {
				System.out.println("No bacteria data found for SampleID: " + sampleID);
			}
		}
		return combinedData;
	}
	
	public static void writeToCSV(String[][] data, String outputFilePath) throws IOException 
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
		
		//header
		writer.write("sampleID,vitalStatus,bactNames\n");
		
		//write rows
		for (String[] row : data)
		{
			writer.write(String.join(",", row) + "\n");
		}
		writer.close();
	}
	
	public static void main(String[] args)
	{
		String metaFilePath = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\TableS9_metadata_KrakenUniq_BIO_Fullset.csv";
		String readFilePath = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\TableS8_T2T_KrakenUniq_BIO_Fullset.csv";
		String outputFilePath = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\parsedPatientData.csv";
		List<String> headers = new ArrayList<>();
		
		try {
			headers = generateHeaders(readFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String, String> patientMetaData = new HashMap<String, String>();
		HashMap<String, List<String>> patientReadData = new HashMap<String, List<String>>();
		
		//read metaTable
		try {
			patientMetaData = parseMetaFile(metaFilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//patientMetaData.forEach((key, value) -> System.out.println(key + ": " + value));
		
		//read readTable
		try {
			patientReadData = parseReadFile(readFilePath, headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//patientReadData.forEach((key, value) -> System.out.println(key + ": " + value));
		
		//code to mash id, vital, names
		String[][] combined;
		combined = combineData(patientMetaData, patientReadData);
//		for (String[] row : combined) {
//			System.out.println(String.join(",", row));
//		}
		
		try
		{
			writeToCSV(combined, outputFilePath);
			System.out.println("Data successfully written to " + outputFilePath);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
}
