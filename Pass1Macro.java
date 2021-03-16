import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pass1Macro 
{
	
	public static void main(String args[]) throws IOException
	{
		File input = new File("macro1.asm");
		
		ArrayList<MNT> mnt = new ArrayList<>();
		ArrayList<KPD> kpd = new ArrayList<>();
		
		ArrayList<Table> p_list = new ArrayList<>();
		
		ArrayList<String> mdt = new ArrayList<>();
		
		
		File fl_2 = new File("pass2_input.txt");
		FileWriter fw_2 = new FileWriter(fl_2);
		BufferedWriter bw_2 = new BufferedWriter(fw_2);
		
		int []k=new int[1];
		k[0]= 1;
		
		int kp=0;
		Pattern pattern = Pattern.compile("&[A-Z]*=|&[A-Z][0-9]=");
		
		try
		{
			Scanner inp_reader = new Scanner(input);
			
			while(inp_reader.hasNext())
			{
				String data = inp_reader.nextLine().trim();
				//System.out.println("DATA: "+data);
				String[] words = data.split("\s+|\t");
				
				/*for(int i=0 ; i < words.length ; i++)
				{
					System.out.println(words[i]+" ");
					
				}*/
				
				if(words.length==1 && words[0].equals("MACRO"))
				{
					MNT m = new MNT();
					int pp=0 ;
					kp=0;
					Table t1 = new Table(),t2=new Table();
					String data1 = inp_reader.nextLine().trim();
					
					//System.out.println("-----------------------------------DATA: "+data1);
					
					String[] words1 = data1.split("\s+|\t");
					
					Table t = new Table();
					
					m.name = words1[0];
					m.kpdtp=k[0]+200;
					for(int i = 1 ; i < words1.length;i++)
					{
						Matcher matcher = pattern.matcher(words1[i]);
						
						if(matcher.find())
						{
							KPD KP = new KPD();
							
							kp++;
							//System.out.println("-----------------------------------DATA2: "+words1[i]);
							String []para = words1[i].split("=");
							
							//System.out.println("-----------------------------------DATA3: "+para[0]);
						//	System.out.println("-----------------------------------DATA4: "+para[1]);
							
							
							KP.name =para[0].substring(1) ; 
							
							if(para[1].endsWith(","))
								KP.value=para[1].substring(0,para[1].length()-1);
							else	
							KP.value =  para[1];
							
							kpd.add(KP);
				
							t.name.add(para[0].substring(1));
							k[0]++;
							
							
						}
						else
						{
							t.name.add(words1[i].substring(1,words1[i].length()-1));
							pp++;
						}
					}
					
					m.pp=pp;
					m.kp=kp;
					
					if(mdt.isEmpty())
					{
						m.mdtp=1;
					}
					else
					{
						m.mdtp=mdt.size()+1;
					}
					
					
					mnt.add(m);
					t.mnt=mnt.size()-1;
					p_list.add(t);
					
					
					do {
					data1 = inp_reader.nextLine().trim();
					
				
					
					String inst=null;
					words1 = data1.split("\s+|\t");
				
					
					if(search_mnt(mnt,words1[0])==-1)
					{
						
						
						if(words1.length==3)
						{
							inst = words1[0]+"\t";
							int ind1 = search_table(t,words1[1].substring(1,words1[1].length()-1));
							int ind2 = search_table(t,words1[2].substring(1));
							inst = inst+"(P,"+(ind1+1)+")\t"+"(P,"+(ind2+1)+")";
							
						}
						else if(words1.length==1)
						{
							inst="MEND";
						}
						
						if(words1.length>3)
							inst=data1;
						
						mdt.add(inst);
					}
					else
					{
						inst="";
						
						if(search_mnt(mnt,words1[0])==-1)
						{
							//System.out.println("-----------------------------------DATA: "+data1);
							inst = inst+words1[0]+"\t";
						
					
							for(int i = 1 ; i<words1.length ;i++ )
							{
								if(words1[i].startsWith("&"))
								{
									int ind = search_table(t,words1[i].substring(1,words1[i].length()-1));
									//System.out.println("-----------------------------------DATA: "+t.name.get(ind));
									inst = inst+"(P,"+(ind+1)+")\t";
								}
								else
								{
									inst = inst+words1[i]+"\t";
								}
							}
						
							mdt.add(inst);
						}
						else
						{
								int ind2 = search_mnt(mnt,words1[0]);
								MNT macro = mnt.get(ind2);
							
								Table aptab = new Table();
							
							for( int i = 1 ; i<words1.length ;i++ )
							{
								Matcher matcher = pattern.matcher(words1[i]);
								if(matcher.find())
								{
									//System.out.println("-----------------------------------Match: "+words1[i]);
									String []para = words1[i].split("=");
									if(para[1].endsWith(","))
										aptab.name.add(para[1].substring(0,para[1].length()-1));
									else	
									aptab.name.add(para[1]);
								}
								else if(words1[i].startsWith("&"))
								{
									
									
									
									int ind = search_table(t,words1[i].substring(1,words1[i].length()-1));
									//System.out.println("-----------------------------------PARA1: "+t.name.get(ind));
									
									aptab.name.add("(P,"+(ind+1)+")");
									
								}
								
								else
								{
									//System.out.println("-----------------------------------PARA2: "+words1[i]);
									if(words1[i].endsWith(","))
									aptab.name.add(words1[i].substring(0,words1[i].length()-1));
									else
									aptab.name.add(words1[i]);
									
								}
							}
							
							if(!(words1.length==(1+macro.kp+macro.pp))) {
								
								int ind3 = macro.kpdtp-201;
							//	System.out.println("-----------------------------------ind2: "+ind2);
								for(int i = ind3 ; i < (ind3+macro.kp);i++)
								{
									aptab.name.add(kpd.get(i).value);
								}
								
							}
						/*	System.out.println("-----------------------------------APTAB--------------------" );
							for(int i = 0 ; i < aptab.name.size();i++)
							{
								System.out.println(aptab.name.get(i));
							}*/
							aptab.mnt=macro.mdtp-1;
							int start = aptab.mnt;
							
							
							for(int j = start ; j < mdt.size() ; j++)
							{
								
								String pass = mdt.get(j).trim();
								String pass2[] = pass.split("\t");
								
								if(pass2[0]=="MEND")
								{
									break;
								}
								else
								{
									
									inst = "+ "+pass2[0]+"\t";
									for(int i = 1 ; i < pass2.length ; i++)
									{
										int ap = Integer.parseInt(pass2[i].substring(3,pass2[i].length()-1));
										inst = inst+aptab.name.get(ap-1)+"\t";
									}
									mdt.add(inst);
								}
							}
							
							
							
						}
					}
					
					
					}while(!words1[0].equals("MEND"));
					
				}
				else
				{
					try
					{

						
						bw_2.write(data);
						bw_2.newLine();
						
					}
					catch(IOException e)
					{
						
					}
				}
				
			}
			bw_2.close();
		}
		catch(IOException e)
		{
			
		}
		
		
		System.out.println("----------------------------------------------------MNT--------------------------------------------------------------");
		try {
		
		File fl = new File("MNT.txt");
		FileWriter fw = new FileWriter(fl);
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		System.out.println("<NAME>\t<#PP>\t<#KP>\t<#KPDTP>\t<#MDTP>");
		bw.write("<NAME>\t<#PP>\t<#KP>\t<#KPDTP>\t<#MDTP>");
		bw.newLine();
		for(MNT m : mnt)
		{
			String str=m.name+"\t"+m.pp+"\t"+m.kp+"\t"+m.kpdtp+"\t\t"+m.mdtp;
			System.out.println(m.name+"\t"+m.pp+"\t"+m.kp+"\t"+m.kpdtp+"\t\t"+m.mdtp);
			bw.write(str);
			bw.newLine();
		}
		bw.close();
		} 
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		
		
		
		
		
		System.out.println("----------------------------------------------------MDT--------------------------------------------------------------");
		
		try {
		
		File fl = new File("MDT.txt");
		FileWriter fw = new FileWriter(fl);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int i=0 ; i < mdt.size();i++)
		{
			String str = (i+1)+".\t"+mdt.get(i);
			System.out.println(str);
			bw.write(str);
			bw.newLine();
		}
		
		bw.close();
		
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		
		
		System.out.println("----------------------------------------------------KPDTAB--------------------------------------------------------------");
		
		
		try {
		
		File fl = new File("KPDT.txt");
		FileWriter fw = new FileWriter(fl);
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		System.out.println("No.\tPara.\tValue");
		
		for(int i = 0 ; i < kpd.size() ; i++)
		{
			String str =(i+1+200)+".\t"+kpd.get(i).name+"\t"+kpd.get(i).value;
			System.out.println(str);
			bw.write(str);
			bw.newLine();
		}
		bw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
		System.out.println("----------------------------------------------------PNTABS--------------------------------------------------------------");
		
		
		try {
		
		File fl = new File("PNTABS.txt");
		FileWriter fw = new FileWriter(fl);
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		
		for(Table t:p_list)
		{
			System.out.println("Name: "+mnt.get(t.mnt).name);
			bw.write("Name: "+mnt.get(t.mnt).name);
			bw.newLine();
			
			for(String name : t.name)
			{
				System.out.println(name);
				bw.write(name);
				bw.newLine();
			}
			
			
		}
		bw.close();
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
		
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
	
	

}
