package org.onetwo.groovy

import org.onetwo.common.utils.FileUtils;


String path = this.getClass().getResource("").getPath()
println "path:$path"
File dir = new File(path)

def Map<String, String> datalist=[:]
dir.eachFile { file ->
	if(file.name.equals("467-result-error-2.txt")){
		file.splitEachLine("\\s") {
			def strs = it[1].split("\\[|\\]")
//			println strs
			/*def ci = new CardInfo()
			ci.lcn=strs[0][0..15]
			ci.idcard=strs[1]
			datalist += ci*/
			datalist[strs[0][0..15]]=strs[1]
		}
	}
}

println "datalist: $datalist"

def index = 0
def newDatalist = []
dir.eachFile { file ->
	if(file.getName().equals("467-result-1.txt")){
		file.splitEachLine("\t") {
//			println it
			if(datalist.containsKey(it[3])){
				it[0] = datalist[it[3]]
				println "$index:$it"
				index++
				newDatalist.add(it)
			}
		}
	}
}

def BufferedWriter newWirter = new File("D:/mydev/workspace/onetwo/labs/test-onetwo/src/test/java/org/onetwo/groovy/467-result-2.txt").newWriter("GBK")
try {
	newDatalist.each {
		println it
		newWirter.writeLine(it.join('\t'))
	}
} finally{
	newWirter.close()
}


