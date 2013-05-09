Compiled from "GenericTypeTest.java"
public class org.onetwo.common.GenericTypeTest extends java.lang.Object
  SourceFile: "GenericTypeTest.java"
  minor version: 0
  major version: 50
  Constant pool:
const #1 = class	#2;	//  org/onetwo/common/GenericTypeTest
const #2 = Asciz	org/onetwo/common/GenericTypeTest;
const #3 = class	#4;	//  java/lang/Object
const #4 = Asciz	java/lang/Object;
const #5 = Asciz	<init>;
const #6 = Asciz	()V;
const #7 = Asciz	Code;
const #8 = Method	#3.#9;	//  java/lang/Object."<init>":()V
const #9 = NameAndType	#5:#6;//  "<init>":()V
const #10 = Asciz	LineNumberTable;
const #11 = Asciz	LocalVariableTable;
const #12 = Asciz	this;
const #13 = Asciz	Lorg/onetwo/common/GenericTypeTest;;
const #14 = Asciz	main;
const #15 = Asciz	([Ljava/lang/String;)V;
const #16 = class	#17;	//  java/util/ArrayList
const #17 = Asciz	java/util/ArrayList;
const #18 = Method	#16.#9;	//  java/util/ArrayList."<init>":()V
const #19 = class	#20;	//  java/util/Date
const #20 = Asciz	java/util/Date;
const #21 = Method	#19.#9;	//  java/util/Date."<init>":()V
const #22 = class	#23;	//  java/sql/Timestamp
const #23 = Asciz	java/sql/Timestamp;
const #24 = Method	#19.#25;	//  java/util/Date.getTime:()J
const #25 = NameAndType	#26:#27;//  getTime:()J
const #26 = Asciz	getTime;
const #27 = Asciz	()J;
const #28 = Method	#22.#29;	//  java/sql/Timestamp."<init>":(J)V
const #29 = NameAndType	#5:#30;//  "<init>":(J)V
const #30 = Asciz	(J)V;
const #31 = Method	#1.#32;	//  org/onetwo/common/GenericTypeTest.upperBound2:(Ljava/util/List;Ljava/util/Date;)V
const #32 = NameAndType	#33:#34;//  upperBound2:(Ljava/util/List;Ljava/util/Date;)V
const #33 = Asciz	upperBound2;
const #34 = Asciz	(Ljava/util/List;Ljava/util/Date;)V;
const #35 = Asciz	args;
const #36 = Asciz	[Ljava/lang/String;;
const #37 = Asciz	list;
const #38 = Asciz	Ljava/util/List;;
const #39 = Asciz	date;
const #40 = Asciz	Ljava/util/Date;;
const #41 = Asciz	time;
const #42 = Asciz	Ljava/sql/Timestamp;;
const #43 = Asciz	LocalVariableTypeTable;
const #44 = Asciz	Ljava/util/List<Ljava/sql/Timestamp;>;;
const #45 = Asciz	Signature;
const #46 = Asciz	<T:Ljava/util/Date;>(Ljava/util/List<TT;>;TT;)V;
const #47 = InterfaceMethod	#48.#50;	//  java/util/List.add:(Ljava/lang/Object;)Z
const #48 = class	#49;	//  java/util/List
const #49 = Asciz	java/util/List;
const #50 = NameAndType	#51:#52;//  add:(Ljava/lang/Object;)Z
const #51 = Asciz	add;
const #52 = Asciz	(Ljava/lang/Object;)Z;
const #53 = Asciz	Ljava/util/List<TT;>;;
const #54 = Asciz	TT;;
const #55 = Asciz	SourceFile;
const #56 = Asciz	GenericTypeTest.java;

{
public org.onetwo.common.GenericTypeTest();
  Code:
   Stack=1, Locals=1, Args_size=1
   0:	aload_0
   1:	invokespecial	#8; //Method java/lang/Object."<init>":()V
   4:	return
  LineNumberTable: 
   line 8: 0

  LocalVariableTable: 
   Start  Length  Slot  Name   Signature
   0      5      0    this       Lorg/onetwo/common/GenericTypeTest;


public static void main(java.lang.String[]);
  Code:
   Stack=4, Locals=4, Args_size=1
   0:	new	#16; //class java/util/ArrayList
   3:	dup
   4:	invokespecial	#18; //Method java/util/ArrayList."<init>":()V
   7:	astore_1
   8:	new	#19; //class java/util/Date
   11:	dup
   12:	invokespecial	#21; //Method java/util/Date."<init>":()V
   15:	astore_2
   16:	new	#22; //class java/sql/Timestamp
   19:	dup
   20:	aload_2
   21:	invokevirtual	#24; //Method java/util/Date.getTime:()J
   24:	invokespecial	#28; //Method java/sql/Timestamp."<init>":(J)V
   27:	astore_3
   28:	aload_1
   29:	aload_3
   30:	invokestatic	#31; //Method upperBound2:(Ljava/util/List;Ljava/util/Date;)V
   33:	return
  LineNumberTable: 
   line 11: 0
   line 12: 8
   line 13: 16
   line 14: 28
   line 15: 33

  LocalVariableTable: 
   Start  Length  Slot  Name   Signature
   0      34      0    args       [Ljava/lang/String;
   8      26      1    list       Ljava/util/List;
   16      18      2    date       Ljava/util/Date;
   28      6      3    time       Ljava/sql/Timestamp;

  LocalVariableTypeTable: length = 0xC
   00 01 00 08 00 1A 00 25 00 2C 00 01 

public static void upperBound2(java.util.List, java.util.Date);
  Signature: length = 0x2
   00 2E 
  Code:
   Stack=2, Locals=2, Args_size=2
   0:	aload_0
   1:	aload_1
   2:	invokeinterface	#47,  2; //InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
   7:	pop
   8:	return
  LineNumberTable: 
   line 20: 0
   line 21: 8

  LocalVariableTable: 
   Start  Length  Slot  Name   Signature
   0      9      0    list       Ljava/util/List;
   0      9      1    date       Ljava/util/Date;

  LocalVariableTypeTable: length = 0x16
   00 02 00 00 00 09 00 25 00 35 00 00 00 00 00 09
   00 27 00 36 00 01 

}

