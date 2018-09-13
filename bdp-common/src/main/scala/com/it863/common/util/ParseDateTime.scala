import java.util.regex.Pattern
  import org.joda.time.DateTime
  import org.joda.time.format.DateTimeFormat

  import scala.collection.mutable

  /**
    * Created by wuzwb on 2017-5-11.
    */
  object ParseDateTime {

    /**
      * 根据date和sql的变量来替换sql by wuzwb
      *
      * @param sql_task
      * @param date
      * @return
      */
    def replaceDateTime(sql_task: String, date: String): String = {

      //如果sql为空则直接返回
      if (date.equals("") || null == date) return sql_task
      //如果符合条件则处理sql否则直接返回原来的sql
      val dp = doFillMap(sql_task, date)
      if (dp != null && (!dp.isEmpty)) {
        //替换$
        var sql = sql_task
        for ((k, v) <- dp) {
          sql = sql.replace(k, v)
        }
        sql
      } else {
        return sql_task
      }
    }

    /**
      * 根据sql获取fillmap  fillmap 里存储 原表达式和要替换的值by wuzwb
      *
      * @param sql_task
      * @return
      */
    def doFillMap(sql_task: String, date: String): mutable.HashMap[String, String] = {

      val fillMap = mutable.HashMap.empty[String, String]
      //${yyyy-mm-dd -?}等的正则表达式
      var reg = "(\\$\\{y{0,4}[:-]*q{0,2}[:-]*m{0,2}[:-]*w{0,2}[:-]*d{0,2}[: -]*h{0,2}[:-]*(mi)?(-(\\d)+?)*?\\})"
      var m = Pattern.compile(reg).matcher(sql_task)

      //找到替换表达式${yyyy-?} 或者${yyyy}
      while (m.find()) {
        val date_source = m.group(1)
        val strs = date_source.split("-")
        var datestr = date_source
        var dt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(date)
        //如果表达式里含有时间差 修改传入时间 ${yyyy-?}
        if (strs.length > 0 && isNumeric(strs(strs.length - 1).replace("}", ""))) {
          val lastIndexOf = date_source.lastIndexOf("-")
          //unit 是截取-前两位作为单位
          val unit = date_source.substring(lastIndexOf - 2, lastIndexOf)
          //offset
          val number = Integer.parseInt(strs(strs.length - 1).replace("}", ""))
          //实际的日期区域除去 ${}的
          datestr = date_source.substring(date_source.indexOf("{") + 1, lastIndexOf)
          dt = unit match {
            case "yy" => dt.plusYears(0 - number)
            case "qq" => dt.plusMonths(0 - number * 3)
            case "mm" => dt.plusMonths(0 - number)
            case "ww" => dt.plusWeeks(0 - number)
            case "dd" => dt.plusDays(0 - number)
            case "hh" => dt.plusHours(0 - number)
            case "mi" => dt.plusMinutes(0 - number)
            case _ => dt
          }
        } else {
          //${yyyy}
          datestr = date_source.substring(date_source.indexOf("{") + 1, date_source.lastIndexOf("}"))
        }
        //填充fillMap
        fillMap += (date_source -> replaceFromdt(dt, datestr))
      }
      fillMap
    }

    /**
      * 在单元里替换 by wuzwb
      *
      * @param dt
      * @param str
      * @return
      */
    def replaceFromdt(dt: DateTime, str: String): String = {
      var q = dt.monthOfYear().get() / 3;
      if (dt.monthOfYear().get() % 3 > 0) {
        q = q + 1
      }
      val re = str.replace("yyyy", fillZero(dt.year().getAsString))
        .replace("mm", fillZero(dt.monthOfYear().getAsString))
        .replace("qq", fillZero(q.toString))
        .replace("ww", fillZero(dt.weekOfWeekyear().getAsString))
        .replace("dd", fillZero(dt.dayOfMonth().getAsString))
        .replace("hh", fillZero(dt.hourOfDay().getAsString))
        .replace("mi", fillZero(dt.minuteOfHour().getAsString))
      return re
    }

    /**
      * 如果是单字符的前面加一个0 比如 1 => 01 by wuzwb
      *
      * @param str
      * @return
      */
    def fillZero(str: String): String = {
      if (str.length == 1) {
        return "0" + str
      } else {
        return str
      }
    }

    /**
      * 判断是否为数字 by wuzwb
      *
      * @param str
      * @return
      */
    def isNumeric(str: String): Boolean = {
      Pattern.compile("[0-9]*").matcher(str).matches()
    }

    /**
      * 替换脚本中的日期变量
      *
      * @param args 脚本 日期变量
      */
    def main(args: Array[String]): Unit = {
      //    val sql=s"""select * frata=$${yyyymmddhh} a}$${yyyy-mmddhh-1}nd id =_date < $${mmddhh-11} $${mmddhhmi-10}"""
      //    val date="2017-08-02 03:10:00"
      if (args.length != 2) throw new RuntimeException("参数不对，参数应该为：script  date  \n 如：,${yyyy},${mm-1} 2017-08-02 03:10:00")
      val res = replaceDateTime(args(0), args(1))
      //    val res = args(0) + args(1)
      System.out.print(res)
    }

  }