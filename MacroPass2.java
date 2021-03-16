import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MacroPass2 
{

	static ArrayList<MNT> mnt = new ArrayList<>();
	static ArrayList<KPD> kpd = new ArrayList<>();
	static ArrayList<Table> p_list = new ArrayList<>();
	static ArrayList<String> mdt = new ArrayList<>();
	static ArrayList<String> pass2_output = new ArrayList<>();
	
	
	public static void main(String args[]) throws IOException 
	{
	
		
		
		prepare_mnt(mnt);
		prepare_mdt(mdt);
		prepare_kpd(kpd);
		prepare_pntab(p_list);
		
		try 
		{
			File input = new File("pass2_input.txt");
			Scanner inp_reader = new Scanner(input);
			inp_reader.nextLine();
			while(inp_reader.hasNext())
			{
				String data = inp_reader.nextLine().trim();
				String words[] = data.split("\s+,|\t+|,\s+");
				if(search_mnt(mnt,words[0])==-1) 
				{
					StringBuilder sb = new StringBuilder();
					for(int i=0 ; i < words.length ; i++)
					{
						sb.append(words[i]+"\t");
					
					}
					
					pass2_output.add(sb.toString());
				}
				else
				{
					/*System.out.println("DATA: "+data);
					
					for(int i=0 ; i < words.length ; i++)
					{
						System.out.println(i+" : "+words[i]+" ");
						
					}*/
					
					int ind = search_mnt(mnt,words[0]);
					MNT m = mnt.get(ind);
					Table aptab = new Table();
					
				//	System.out.println(m.pp+" "+m.kp+" "+m.kpdtp+" "+m.mdtp);
					
				//	System.out.println("P:"+ind);
					
					
					
					int index = m.mdtp-1;
					String inst = mdt.get(index).trim();
					String inst_1[] = inst.split("\t+");
					
					for(int i = 1 ; i <=m.pp;i++)
					{
						aptab.name.add(words[i]);
					}
					
					if(words.length ==(m.pp+m.kp+1))
					{
						for(int i = (m.pp+1) ; i < words.length;i++)
						{
							aptab.name.add(words[i].substring(3));
						}
						
					}
					else
					{
						if(ind==mnt.size()-1)
						{
							for(int i = (m.kpdtp-1); i < kpd.size();i++ )
							{
								aptab.name.add(kpd.get(i).value);
							}
						}
						else
						{
							for(int i = (m.kpdtp-1); i < mnt.get(ind+1).kpdtp-1;i++ )
							{
								aptab.name.add(kpd.get(i).value);
							}
						}
					}
					
					System.out.println("APTAB:\t"+mnt.get(ind).name);
					for(int i = 0 ; i<aptab.name.size();i++)
					{
						System.out.println(aptab.name.get(i));
					}
					
					
					while(!inst_1[0].equals("MEND"))
					{
						//System.out.println("INST:\t"+inst);
						
						 String code="";
						/* for(int i = 0 ; i < inst_1.length;i++)
						 {
							 System.out.println("INST0:\t"+inst_1[i]);
						 }*/
						 
								 
						 if(inst.startsWith("+"))
						 {
							 code = inst_1[0]+"\t";
							 
							 for(int i = 1 ; i < inst_1.length ; i++)
							 {
								 if(inst_1[i].startsWith("(P,"))
								 {
									 int ap_ind = Integer.parseInt(inst_1[i].substring(3,(inst_1[i].length()-1)));
									 code = code+aptab.name.get((ap_ind-1))+",\t";
								 }
								 else
								 {
									 code = code + inst_1[i]+",\t";
								 }
							 }
						 }
						 else
						 {
							 code = "+\s"+inst_1[0]+"\t";
							 
							 for(int i = 1 ; i < inst_1.length ; i++)
							 {
								 if(inst_1[i].startsWith("(P,"))
								 {
									 int ap_ind = Integer.parseInt(inst_1[i].substring(3,(inst_1[i].length()-1)));
									 code = code+aptab.name.get((ap_ind-1))+",\t";
									 //System.out.println("INST1:\t"+inst_1[i]+"\t"+inst_1[i].length()+"\t"+ap_ind);
								 }
								 else
								 {
									 code = code + inst_1[i]+",\t";
								 }
							 }
						 }
						 
						 
						
						 pass2_output.add(code.substring(0,code.length()-2));
						 index++;
						 inst = mdt.get(index).trim();
						 inst_1 = inst.split("\t+");
					}
					
				}
				
			}
		}
		catch(IOException e)
		{
			
		}
		System.out.println("----------------------------------------------------PASS-2 OUTPUT--------------------------------------------------------------");
		File fl = new File("pass2_output.txt");
		FileWriter fw = new FileWriter(fl);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int i = 0 ; i < pass2_output.size();i++)
		{
			bw.write((i+1)+".\t"+pass2_output.get(i));
			System.out.println((i+1)+".\t"+pass2_output.get(i));
			bw.newLine();
		}
		bw.close();
		
	
	
	}
	
	public static int search_mnt(ArrayList<MNT> mnt,String name)
	{
		
		for(int i=0 ; i < mnt.size();i++)
		{
			if(mnt.get(i).name.equals(name))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	public static int search_table(Table t,String name)
	{
		
		for(int i = 0 ; i < t.name.size();i++)
		{
			if(t.name.get(i).equals(name))
			{
				return i;
			}
		}
		return -1;
	}
	
	public static int search_list(ArrayList<Table> p_list,int n)
	{
		
		for(int i = 0 ; i < p_list.size();i++)
		{
			if(p_list.get(i).mnt==n)
			{
				return i;
			}
		}
		
		
		return -1;
		
	}
	
	
	public static void prepare_mnt(ArrayList<MNT> mnt)
	{
		try
		{
			File input = new File("MNT.txt");
		
			Scanner inp_reader = new Scanner(input);
			inp_reader.nextLine();
		
			while(inp_reader.hasNext())
			{
				String data = inp_reader.nextLine().trim();
				String words[] = data.split("\t+");
				/*for(int i=0 ; i < words.length ; i++)
				{
					System.out.println(words[i]+" ");
													
				}*/
				MNT m = new MNT();
				m.name = words[0];
				m.pp  = Integer.parseInt(words[1]);
				m.kp  = Integer.parseInt(words[2]);
				m.kpdtp  = Integer.parseInt(words[3])-200;
				m.mdtp  = Integer.parseInt(words[4]);
				
				mnt.add(m);
				
				
			}
			
			/*System.out.println("----------------------------------------------------MNT--------------------------------------------------------------");
			
		
			System.out.println("<NAME>\t<#PP>\t<#KP>\t<#KPDTP>\t<#MDTP>");
			
			for(MNT m : mnt)
			{
				String str=m.name+"\t"+m.pp+"\t"+m.kp+"\t"+m.kpdtp+"\t\t"+m.mdtp;
				System.out.println(m.name+"\t"+m.pp+"\t"+m.kp+"\t"+(m.kpdtp+200)+"\t\t"+m.mdtp);
				
			}*/
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
	
	public static void prepare_mdt(ArrayList<String> mdt)
	{
		try
		{
			File input = new File("MDT.txt");
		
			Scanner inp_reader = new Scanner(input);
			
		
			while(inp_reader.hasNext())
			{
				StringBuilder sb =  new StringBuilder();
				String data = inp_reader.nextLine().trim();
				String words[] = data.split("\t+");
				for(int i=1 ; i < words.length ; i++)
				{
					//System.out.println(words[i]);
					sb.append(words[i]+"\t");							
				}
				
				mdt.add(sb.toString());
				
				
			}
			/*System.out.println("----------------------------------------------------MDT--------------------------------------------------------------");
			for(int i=0 ; i < mdt.size();i++)
			{
				String str = (i+1)+".\t"+mdt.get(i);
				System.out.println(str);
			}*/
			
			
	}
	catch(IOException e)
	{
		System.out.println(e);
	}
	}
	
	
	public static void prepare_kpd(ArrayList<KPD> kpd)
	{
		try
		{
			File input = new File("KPDT.txt");
		
			Scanner inp_reader = new Scanner(input);
			
		
			while(inp_reader.hasNext())
			{
				
				String data = inp_reader.nextLine().trim();
				String words[] = data.split("\t+");
				
				KPD k = new KPD();
				k.name = words[1];
				k.value = words[2];
				
				kpd.add(k);
				
				
			}
			
			/*System.out.println("----------------------------------------------------KPDT--------------------------------------------------------------");
			for(int i = 0 ; i < kpd.size() ; i++)
			{
				String str =(i+1+200)+".\t"+kpd.get(i).name+"\t"+kpd.get(i).value;
				System.out.println(str);
				
			}*/
			
			
	}
	catch(IOException e)
	{
		System.out.println(e);
	}
	}
	
	public static void prepare_pntab(ArrayList<Table> p_list)
	{
		try
		{
			File input = new File("PNTABS.txt");
		
			Scanner inp_reader = new Scanner(input);
			Table T = null;
		
			int mnt_c = 0;
			while(inp_reader.hasNext())
			{
				int para=0;
				String data = inp_reader.nextLine().trim();
				String words[] = data.split("\t+");
				
				//System.out.println("Data: "+data);
				
					if(words[0].startsWith("Name:"))
					{
						//System.out.println("Data: "+mnt_c);
						 T = new Table();
						T.mnt = mnt_c;
						mnt_c+=1;
						MNT m = mnt.get(T.mnt);
						para = m.pp+m.kp;
						
						for(int i = 0 ; i < para ; i++)
						{
							String data1 = inp_reader.nextLine().trim();
							
							T.name.add(data1);
						}
						
					}
					//T.name.add(words[i]);
					
					
			
				p_list.add(T);
				
				
			}
			
			/*for(Table t:p_list)
			{
				System.out.println("Name: "+mnt.get(t.mnt).name+"\t"+t.mnt);
				
				
				for(String name : t.name)
				{
					System.out.println(name);
					
				}
				
				
			}*/
			
			
	}
	catch(IOException e)
	{
		System.out.println(e);
	}
		
		
	}
	
}
