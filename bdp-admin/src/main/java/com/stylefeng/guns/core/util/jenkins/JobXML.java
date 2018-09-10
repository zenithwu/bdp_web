package com.stylefeng.guns.core.util.jenkins;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;

public class JobXML {


    private Document jobXml;


    /***
     * 初始化job XML
     * @param description
     * @param command
     * @return
     * @throws DocumentException
     */
    public JobXML init(String description,String command) {
       return init(description,Constant.DEFALUT_PARAM,command);
    }
    /***
     * 初始化job XML
     * @param description
     * @param params 参数列表逗号隔开
     * @param command
     * @return
     * @throws DocumentException
     */
    public JobXML init(String description,String params,String command) {

        Document doc=DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        //创建根元素
        Element root = doc.addElement("project");
        //添加子节点和设置属性
        //description
        root.addElement("description").setText(description);
        //triggers
        root.addElement("triggers");
        //properties+params
        root.addElement("properties").addElement("hudson.model.ParametersDefinitionProperty")
                .addElement("parameterDefinitions");
        if(StringUtils.isNotEmpty(params)){
            for (String param:params.split(",")
                    ) {
                if (StringUtils.isNotEmpty(params)) {
                    root.element("properties").element("hudson.model.ParametersDefinitionProperty")
                            .element("parameterDefinitions")
                            .addElement("hudson.model.StringParameterDefinition")
                            .addElement("name").setText(param);
                }
            }
        }
        //disabled
        root.addElement("disabled").setText("false");
        //builders
        root.addElement("builders").addElement("hudson.tasks.Shell")
                .addElement("command").setText(command);
        this.jobXml=doc;
        return this;
    }

    /***
     * 初始化job XML
     * @param xml
     * @return
     * @throws DocumentException
     */
    public JobXML init(String xml) throws DocumentException {
        // 创建saxReader对象
        SAXReader reader = new SAXReader();
        // 通过read方法读取一个文件 转换成Document对象
        Document doc=reader.read(new InputSource(new StringReader(xml)));
        this.jobXml=doc;
        return this;
    }

    /***
     *
     * @param projects test2,test3
     * @param params time_hour
     */
    public JobXML setUpstreams(String projects,String params){
        Element triggers = this.jobXml.getRootElement().element("triggers");
        if(StringUtils.isNotEmpty(projects)) {
            if (null == triggers.element("org.lonkar.jobfanin.FanInReverseBuildTrigger")) {
                Element ele = triggers.addElement("org.lonkar.jobfanin.FanInReverseBuildTrigger")
                        .addAttribute("plugin", Constant.PLUGIN_VERSION);

                ele.addElement("upstreamProjects").setText(projects);
                ele.addElement("upstreamParams").setText(params);
                ele.addElement("spec");
                Element threshold = ele.addElement("threshold");
                threshold.addElement("name").setText("SUCCESS");
                threshold.addElement("ordinal").setText("0");
                threshold.addElement("color").setText("BLUE");
                threshold.addElement("completeBuild").setText("true");
            } else {
                Element procEle = triggers.element("org.lonkar.jobfanin.FanInReverseBuildTrigger");
                procEle.element("upstreamProjects").setText(projects);
                procEle.element("upstreamParams").setText(params);
            }
        }else{

            //删除 依赖设置
            if (null != triggers.element("org.lonkar.jobfanin.FanInReverseBuildTrigger")) {
                triggers.remove(triggers.element("org.lonkar.jobfanin.FanInReverseBuildTrigger"));
            }

        }
        return this;
    }
    /***
     *
     * @param projects test2,test3
     */
    public JobXML setUpstreams(String projects){
        return setUpstreams(projects,Constant.DEFALUT_PARAM);
    }

    public JobXML setBuilders(String command){
        this.jobXml.getRootElement().element("builders").element("hudson.tasks.Shell")
                .element("command").setText(command);
        return this;
    }

    public JobXML setDescription(String description){
        this.jobXml.getRootElement().element("description").setText(description);
        return this;
    }

    public JobXML setDisabled(String disabled){
        this.jobXml.getRootElement().element("disabled").setText(disabled);
        return this;
    }

    public JobXML setCrontab(String crontab){

        Element triggers = this.jobXml.getRootElement().element("triggers");
        if(StringUtils.isNotEmpty(crontab)) {
            if (null == triggers.element("hudson.triggers.TimerTrigger")) {
                triggers.addElement("hudson.triggers.TimerTrigger").addElement("spec").setText(crontab);
            } else {
                triggers.element("hudson.triggers.TimerTrigger").addElement("spec").setText(crontab);
            }
        }else{
            //删除crontab
            if (null != triggers.element("hudson.triggers.TimerTrigger")) {
                triggers.remove(triggers.element("hudson.triggers.TimerTrigger"));
            }

        }
        return this;
    }


    public String getJobXml(){
        return this.jobXml.asXML();
    }
}
