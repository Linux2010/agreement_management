package com.rytc.common.util;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.rytc.system.domain.Dept;
import com.rytc.web.domain.Agreement;
import com.rytc.web.domain.BorrowUser;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;



/**
* 新版借款合同PDF文件生成工具类
* Created by lzp on 2017/5/31.
*/
public class NewLoanContractUtil {
    private static final String font_ST = "STSongStd-Light";
    private static final String font_Un = "UniGB-UCS2-H";
    /**
     *
     * @param agreement 协议信息
     * @param dept 部门信息
     * @param borrowUser 借款人信息
     * @param borrowUser2 共同借款人信息
     * @return
     */
    public static ByteArrayOutputStream getPdfIO(Agreement agreement,Dept dept,BorrowUser borrowUser,BorrowUser borrowUser2) {
    	String signDate = agreement.getSignDate();
    	String[] sDate = signDate.split("-");
    	
    	BigDecimal manageFee = new BigDecimal(agreement.getManageFee());
    	BigDecimal financFee = new BigDecimal(agreement.getFinancFee());
    	
    	BigDecimal totalFee = manageFee.add(financFee).setScale(2, BigDecimal.ROUND_HALF_UP);
    	
        Document document = new Document();
        PdfWriter writer = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            setFooter(writer,agreement.getAgreementNo());
            BaseFont bfChinese = BaseFont.createFont(font_ST, font_Un, false);
            Font impressFont = new Font(bfChinese, 14, Font.BOLD);
            Font impressBFont = new Font(bfChinese, 22, Font.BOLD);
            Font pressFont = new Font(bfChinese, 14, Font.NORMAL);

            // 打开文档，将要写入内容
            document.open();
            Paragraph titleP = new Paragraph("借款服务协议", impressBFont);
            titleP.setAlignment(Element.ALIGN_CENTER);
            document.add(titleP);
            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("本协议由以下三方于（"+sDate[0]+"）年（"+sDate[1]+"）月（"+sDate[2]+"）日在中华人民共和国", pressFont));
            document.add(new Paragraph(dept.getAddress()+"签署并履行：", pressFont));
            
            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("甲方（借款人）："+borrowUser.getUserName(), pressFont));
            document.add(new Paragraph("身份证号码："+borrowUser.getIdNum(), pressFont));
            document.add(new Paragraph("地址："+borrowUser.getProvince()+borrowUser.getCity()+borrowUser.getCounty()+borrowUser.getAddress(), pressFont));
            document.add(new Paragraph("联系电话："+borrowUser.getPhone(), pressFont));

            if(borrowUser2 != null) {
            	
            	document.add(new Paragraph("    ", pressFont));
            	document.add(new Paragraph("甲方（共同借款人）："+borrowUser2.getUserName(), pressFont));
//            	document.add(new Paragraph("XXXXX", pressFont));
            	document.add(new Paragraph("身份证号码："+borrowUser2.getIdNum(), pressFont));
//            	document.add(new Paragraph("XXXXX", pressFont));
            	document.add(new Paragraph("地址：", pressFont));
            	document.add(new Paragraph(borrowUser2.getProvince()+borrowUser2.getCity()+borrowUser2.getCounty()+borrowUser2.getAddress(), pressFont));
            	document.add(new Paragraph("联系电话："+borrowUser2.getPhone(), pressFont));
//            	document.add(new Paragraph("XXXXX", pressFont));
            }
            
            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("乙方：荣耀时代（北京）信息咨询有限公司", pressFont));
            document.add(new Paragraph("地址：北京市门头沟区三家店东街51号CZ0701室", pressFont));

            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("丙方：黑龙江荣耀天成投资有限公司", pressFont));
            document.add(new Paragraph("地址：黑龙江省哈尔滨市南岗区先锋路509号5层1号", pressFont));
            
            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("鉴于：甲方有一定的资金需求；乙方为甲方提供信用咨询、评估以及贷后管", pressFont));
            document.add(new Paragraph("理等服务；乙方推荐甲方在丙方网站“1号领投”（域名为https://www.rytc.cn/", pressFont));
            document.add(new Paragraph("及其移动应用APP）上向特定的出借人匹配借款并提供相应的贷后管理服务，丙", pressFont));
            document.add(new Paragraph("方收到甲方信息后在丙方平台上展示并发起融资，撮合甲方与出借人达成借款协", pressFont));
            document.add(new Paragraph("议，为甲方提供个性化的融资服务。如经乙方推荐丙方审核，前述借款匹配成功，", pressFont));
            document.add(new Paragraph("甲方、丙方及出借人将通过积“1号领投”平台共同签署线上《借款协议》（下", pressFont));
            document.add(new Paragraph("称“《借款协议》”），还款金额及扣款时间具体以《还款明细表》载明为准。", pressFont));
            document.add(new Paragraph("	经各方沟通，现上述三方就以上事项及相关服务达成一致，特订立本协议。", pressFont));


            document.add(new Paragraph("第一条	借款事项", impressFont));
            document.add(new Paragraph("1.1 乙方推荐甲方在“1号领投”平台借款，拟借款金额为人民币（大写）", pressFont));
            document.add(new Paragraph(PriceUtil.change(agreement.getBorrowAmount())+"（RMB￥"+agreement.getBorrowAmount()+"元），借款期限为"+agreement.getTerm()+"个月，借款利率："+agreement.getRate()+"年", pressFont));
            document.add(new Paragraph("个月，借款利率："+agreement.getRate(), pressFont));
//            document.add(new Paragraph("XXXXX", pressFont));
            document.add(new Paragraph("化），借款用途："+agreement.getBorrowUse()+"，相关内容详见《还款管理服务说明书》。", pressFont));
            document.add(new Paragraph("1.2 甲方按照每月等额本息方式进行还款。", pressFont));
            document.add(new Paragraph("1.3 甲方为多主体（包括自然人及法人）共同借款的，任一主体均应履行本协议", pressFont));
            document.add(new Paragraph("及《借款协议》项下的义务，对全部合同义务及债务承担连带责任，本协议其他", pressFont));
            document.add(new Paragraph("各方及出借人有权向任一主体主张履行合同义务或者追索全部债务。", pressFont));
            document.add(new Paragraph("1.4 账户开立：甲方指定"+agreement.getRepayOpenAcctName()+"（身份证："+agreement.getRepayCard()+"）", pressFont));
            document.add(new Paragraph("作为代表人在“1号领投”平台进行用户注册并在支付机构（即与丙方合作的持", pressFont));
            document.add(new Paragraph("有第三方支付牌照的公司或银行）开设与甲方在“1号领投”账户关联的交易结", pressFont));
            document.add(new Paragraph("算资金管理账户，该代表人接受甲方委托开立账户并签署相关协议，甲方确认该", pressFont));
            document.add(new Paragraph("代表人在“1号领投”平台的文件签署行为由各方共同承担法律责任，甲方无条", pressFont));
            document.add(new Paragraph("件接受该代表人签署的电子文件的约束，该代表人在“1号领投”平台与借款事", pressFont));
            document.add(new Paragraph("项相关的一切行为均由甲方负责。", pressFont));
            document.add(new Paragraph("1.5 借款交付：自出借人出借款项进入甲方上述交易结算资金管理账户即视为出", pressFont));
            document.add(new Paragraph("借人已经完成借款交付义务，债权即刻成立，甲方是否提现（将交易结算资金管", pressFont));
            document.add(new Paragraph("理账户中的资金转入甲方银行账户）可自行安排且不影响出借人债权的效力。", pressFont));
            document.add(new Paragraph("1.6 借款要约：鉴于甲方将通过“1号领投”平台进行融资并与丙方及出借人共", pressFont));
            document.add(new Paragraph("同签署线上《借款协议》，甲方在此声明：本《借款服务协议》第一条所约定内", pressFont));
            document.add(new Paragraph("容与所附“《借款协议》文本”、《还款管理服务说明书》（文本以附件为准，", pressFont));
            document.add(new Paragraph("甲方已充分阅读并理解）共同构成本次借款的要约，甲方指令丙方在“1号领投”", pressFont));
            document.add(new Paragraph("网站一次或者多次向出借人发出本要约，甲方在拟借款金额范围内无条件接受出", pressFont));
            document.add(new Paragraph("借人承诺金额并以附件“《借款协议》文本”与出借人最终达成《借款协议》，", pressFont));
            document.add(new Paragraph("甲方将严格依照《借款协议》内容履行义务。本要约不得撤回。", pressFont));
            document.add(new Paragraph("1.7 有关授权及还款：甲方同意根据本协议所约定的日期及金额，向甲方的还款", pressFont));
            document.add(new Paragraph("账户中存入相应款项，并通过丙方平台进行偿还。", pressFont));
            document.add(new Paragraph("1.8 丙方将按照本协议、《借款协议》及其他文件的约定，将通过其合作的支付", pressFont));
            document.add(new Paragraph("机构每月从甲方还款账户中准确支付相应的数额；若出现非因甲方原因而多支付", pressFont));
            document.add(new Paragraph("的情形时，由丙方负责解决，丙方对多支付的部分按照同期银行存款利息对甲方", pressFont));
            document.add(new Paragraph("进行退还。", pressFont));
            
            document.add(new Paragraph("第二条	服务费用", impressFont));
            document.add(new Paragraph("2.1 鉴于在为甲方匹配“1号领投”平台借款的过程中，由乙方负责将甲方推荐", pressFont));
            document.add(new Paragraph("至“1号领投”平台，并在甲方取得借款后向甲方提供贷后管理服务，包括但不", pressFont));
            document.add(new Paragraph("限于还款提示、还款指导等，甲方应向乙方支付相应的管理费（下称“乙方管理", pressFont));
            document.add(new Paragraph("费”）；同时丙方为甲方提供平台匹配借款的居间服务，因而甲方应向丙方支付", pressFont));
            document.add(new Paragraph("融资服务费（下称“丙方融资服务费”）。", pressFont));
            document.add(new Paragraph("2.2 前述服务费用总额为人民币（大写）："+PriceUtil.change(totalFee.doubleValue())+"（RMB："+totalFee+"元），", pressFont));
            document.add(new Paragraph("其中包含乙方管理费即人民币（大写）："+PriceUtil.change(manageFee.doubleValue())+"（RMB："+manageFee+"元）；", pressFont));
            document.add(new Paragraph("丙方融资服务费=融资金额*【"+agreement.getFinancRate()+"】%即人民币（大写）："+PriceUtil.change(financFee.doubleValue())+"（RMB：", pressFont));
            document.add(new Paragraph(financFee+"元）。", pressFont));
            document.add(new Paragraph("2.3 支付时间：甲方同意在出借人通过“1号领投”平台向甲方支付借款本金的", pressFont));
            document.add(new Paragraph("当日向乙、丙双方支付上述服务费用。", pressFont));
            document.add(new Paragraph("2.4 支付方式：甲方同意并授权，前述费用由甲方取得借款时自其取得的借款中", pressFont));
            document.add(new Paragraph("由丙方委托支付机构自甲方开设的与“1号领投”账户相关联的交易结算资金管", pressFont));
            document.add(new Paragraph("理账户中进行支付。其中，融资服务费、管理费一次性收取，一旦收取不予退还。", pressFont));
            document.add(new Paragraph("2.5 乙、丙双方为甲方提供相关服务的期间自本协议签署之日起，至借款相关债", pressFont));
            document.add(new Paragraph("务履行完毕之日止。", pressFont));
            
            document.add(new Paragraph("第三条	逾期与催收服务", impressFont));
            document.add(new Paragraph("3.1 本协议所述“逾期”，如无特别说明，是指甲方未能按照本协议附件《还款", pressFont));
            document.add(new Paragraph("管理服务说明书》在扣款日期将相应还款金额存入其指定还款账户导致应还本息", pressFont));
            document.add(new Paragraph("未能如期归还的情形。", pressFont));
            document.add(new Paragraph("3.2 甲方同意并认可，甲方逾期的情形下乙方即可启动催收服务，甲方除应偿还", pressFont));
            document.add(new Paragraph("应还本息外，还须向乙方支付下述“催收服务费”。", pressFont));
            document.add(new Paragraph("3.3催收服务费：按照应付未付款项总额的0.5%每日计算，若计算所得低于100", pressFont));
            document.add(new Paragraph("元的，按100元计，每期单独计算。甲方同意按照乙方指定的时间和方式支付“催", pressFont));
            document.add(new Paragraph("收服务费”。", pressFont));
            
            document.add(new Paragraph("第四条	提前还款", impressFont));
            document.add(new Paragraph("4.1 若甲方提前还款给债权人：需提前三个工作日与乙方联系，提出书面申请，", pressFont));
            document.add(new Paragraph("约定提前还款时间（原扣款日当日及节假日期间不执行提前还款的支付，如约定", pressFont));
            document.add(new Paragraph("的提前还款时间为节假日或原扣款日当日的，则顺延至之后的第一个工作日执", pressFont));
            document.add(new Paragraph("行），甲方需在约定的时间前把相应的款项存入指定账户。", pressFont));
            document.add(new Paragraph("4.2 乙方认为甲方偿付能力、偿付意愿已经发生实质性的不利变化且该等变化将", pressFont));
            document.add(new Paragraph("会或可能会损害债权人在《借款协议》项下权益实现的，或根据《借款协议》、", pressFont));
            document.add(new Paragraph("《借款声明》的约定触发提前还款的，则乙方有权指定某一扣款日为提前还款日，", pressFont));
            document.add(new Paragraph("并要求甲方立即将相当于剩余本金和届期应付利息总额的款项存入甲方还款账", pressFont));
            document.add(new Paragraph("户，由乙方完成支付并代甲方在“1号领投”平台进行提前还款操作，将款项交", pressFont));
            document.add(new Paragraph("付甲方的债权人。", pressFont));
            document.add(new Paragraph("4.3 提前结清时当期利息应足额支付，不足一期的按一期计算。", pressFont));
            
            document.add(new Paragraph("第五条	甲方权利与义务", impressFont));
            document.add(new Paragraph("5.1 甲方有权向乙方了解其信用评审进度及结果；", pressFont));
            document.add(new Paragraph("5.2 甲方在申请及实现借款的全过程中，必须如实向乙方、丙方提供借款所需的", pressFont));
            document.add(new Paragraph("个人信息；", pressFont));
            document.add(new Paragraph("5.3 甲方在乙方建立个人信用账户，授权乙方基于甲方提供的信息及乙方独立获", pressFont));
            document.add(new Paragraph("取的信息来管理甲方的信用信息；", pressFont));
            document.add(new Paragraph("5.4 甲方同意依法设立的征信机构将其在丙方平台上产生的信用信息，纳入各征", pressFont));
            document.add(new Paragraph("信系统和征信中心的金融信用信息基础数据库，用于相关法律、法规、规章和规", pressFont));
            document.add(new Paragraph("范性文件规定的用途；并同意乙方、丙方向征信机构查询甲方在各征信系统中的", pressFont));
            document.add(new Paragraph("个人信用信息，或通过征信机构向征信中心查询甲方的个人信用信息；", pressFont));
            document.add(new Paragraph("5.5 甲方知悉并同意遵守“1号领投”平台运营规则，也同意以电子协议方式与", pressFont));
            document.add(new Paragraph("出借人签署在线《借款协议》并无条件接受上述《借款协议》的约束；", pressFont));
            document.add(new Paragraph("5.6 甲方应按照本协议的规定向乙方支付管理费、向丙方支付融资服务费；", pressFont));
            document.add(new Paragraph("5.7 甲方同意，甲方成功借款后，丙方依据出借人或“1号领投”平台的委托协", pressFont));
            document.add(new Paragraph("调甲方按照约定期限及金额进行还款，甲方有义务无条件及时配合丙方工作；", pressFont));
            document.add(new Paragraph("5.8 甲方同意与其有借款协议关系的出借人可随时在同等条件下将债权转让给第", pressFont));
            document.add(new Paragraph("三方；", pressFont));
            document.add(new Paragraph("5.9 甲方同意，为了更好的履行合同义务、保护出借人权益及进行数据核对与保", pressFont));
            document.add(new Paragraph("管，授权丙方查阅、调取、保管甲方的交易结算资金管理账户的注册信息、交易", pressFont));
            document.add(new Paragraph("信息（含交易对手及账户信息）、流水记录，并同意丙方依据本协议及《借款协", pressFont));
            document.add(new Paragraph("议》相关各方的合理需求将此等信息、记录出示、提交给相关主体，同时甲方同", pressFont));
            document.add(new Paragraph("意丙方合作的资金存管银行可依据此等授权径行将相关账户流水记录提供给丙方", pressFont));
            document.add(new Paragraph("而无需甲方另行确认；", pressFont));
            document.add(new Paragraph("5.10 甲方同意，在甲方未能及时履约情形下，乙方有权委托本协议外的其他服务", pressFont));
            document.add(new Paragraph("机构为出借人提供贷后管理及催收服务，相关费用由甲方负担，收费标准以其他", pressFont));
            document.add(new Paragraph("服务机构公示为准；", pressFont));
            
            document.add(new Paragraph("第六条	乙方权利与义务", impressFont));
            document.add(new Paragraph("6.1 乙方应协助甲方提供各项信息并通过乙方合作的专门服务机构独立获得的甲", pressFont));
            document.add(new Paragraph("方的相关信息；", pressFont));
            document.add(new Paragraph("6.2 在上述及借款存续提供贷后管理服务的过程中，乙方有权将甲方资料、信息、", pressFont));
            document.add(new Paragraph("履约情况提供给第三方征信机构，以供有关单位、部门或个人依法查询和使用。", pressFont));
            document.add(new Paragraph("6.3 乙方应当根据本协议及有关文件的约定为甲方提供借款及还款相关的咨询及", pressFont));
            document.add(new Paragraph("管理服务，并基于其提供的服务向甲方收取相应的管理费。", pressFont));
            document.add(new Paragraph("6.4 甲方、丙方同意并理解，如乙方根据甲方提供的信息，有理由判断并认为甲", pressFont));
            document.add(new Paragraph("方存在欺诈可能的，乙方有权在甲方根据本协议第一条第5）款约定获得出借人", pressFont));
            document.add(new Paragraph("交付的借款前的任一时间终止本协议且无需承担任何责任，甲方的任何损失（如", pressFont));
            document.add(new Paragraph("有）应由甲方自行承担。", pressFont));
            document.add(new Paragraph("6.5 乙方应依据甲方与出借人签署的《借款协议》载明之还款计划进行还款提示，", pressFont));
            document.add(new Paragraph("并解答甲方在履行还款义务过程中遇见的问题，并提供必要的协助。", pressFont));
            
            document.add(new Paragraph("第七条	丙方权利与义务", impressFont));
            document.add(new Paragraph("7.1 享有要求甲方对融资过程中提供的相关资料、文件的真实性负责的权利。", pressFont));
            document.add(new Paragraph("7.2 甲方未能依约履行还款义务情形下，享有要求乙方协助敦促甲方还款的权利。", pressFont));
            document.add(new Paragraph("7.3 负有依据甲方、乙方要求向其披露融资进展情况的义务。", pressFont));
            document.add(new Paragraph("7.4 履行本协议约定的安排上线融资的义务，提供约定的居间服务义务以及收取", pressFont));
            document.add(new Paragraph("融资服务费的权利。", pressFont));
            
            document.add(new Paragraph("第八条	甲方指定账户", impressFont));
            document.add(new Paragraph("8.1 甲方指定如下银行账户与交易结算资金管理账户进行绑定，作为收取借款、", pressFont));
            document.add(new Paragraph("支付还款等本协议项下资金往来的专用银行账户：", pressFont));
            document.add(new Paragraph("户名："+agreement.getRepayOpenAcctName()+"；账号："+agreement.getRepayAcctNo(), pressFont));
            document.add(new Paragraph("；开户银行（精确到分行、支行、储蓄所）："+agreement.getRepayOpenAcctBank(), pressFont));
            document.add(new Paragraph("8.2 甲方须确保该账户为甲方名下合法有效的银行账户，如甲方需要变更指定账", pressFont));
            document.add(new Paragraph("户，须在扣款日前至少7个工作日向乙方提出申请，并签署《借款人信息变更书》。", pressFont));
            document.add(new Paragraph("否则因甲方不及时提供上述变更信息导致甲方未能及时实现还款的，其产生的逾", pressFont));
            document.add(new Paragraph("期罚息及催收服务费等约定费用由甲方承担。", pressFont));
            document.add(new Paragraph("8.3在还款过程中，甲方有义务配合为达成还款而进行的包括但不限于账户验证、", pressFont));
            document.add(new Paragraph("账户变更、身份验证等事项，因甲方不配合而造成的未能正常还款的罚息、违约", pressFont));
            document.add(new Paragraph("金、催收服务费等损失由甲方负责。", pressFont));
            
            document.add(new Paragraph("第九条	违约规定", impressFont));
            document.add(new Paragraph("9.1 任何一方违反本协议的约定，使得本协议全部或部分不能履行的，违约方应", pressFont));
            document.add(new Paragraph("当承担全部责任，并赔偿守约方因此遭受的损失（包括由此产生的诉讼费用和合", pressFont));
            document.add(new Paragraph("理律师费）；如各方均违约，应当根据实际情况各自承担相应的责任。特别提醒", pressFont));
            document.add(new Paragraph("的是，甲方逾期还款达到或者超过30天，或连续或累计逾期三次的，乙方有权依", pressFont));
            document.add(new Paragraph("《借款协议》之约定宣告借款提前到期，甲方应在三日内一次性支付全部借款本", pressFont));
            document.add(new Paragraph("金、利息、催收服务费及其它因此产生的相关费用。", pressFont));
            document.add(new Paragraph("9.2 甲方未按本协议约定在其姓名、身份证号、地址、电话号码、工作单位等信", pressFont));
            document.add(new Paragraph("息发生变更后及时通知乙方及丙方，由此给乙方、丙方及债权人造成损失的，应", pressFont));
            document.add(new Paragraph("当承担相应的赔偿责任。", pressFont));
            document.add(new Paragraph("9.3 甲方同意，若甲方发生《借款协议》项下违约情形的，甲方债权人、乙方及", pressFont));
            document.add(new Paragraph("丙方为保障债权人的合法利益有权向甲方进行催收、追回借款本息以及相关费用。", pressFont));
            document.add(new Paragraph("因催收、追回借款本息以及相关费用导致甲方债权人、乙方、丙方产生费用支出", pressFont));
            document.add(new Paragraph("的（包括但不限于合理的律师费、诉讼费、取证费、公证费、第三方代理服务费", pressFont));
            document.add(new Paragraph("用、催收所发生的交通费、差旅费等）应由甲方承担。", pressFont));
            
            document.add(new Paragraph("第十条	变更通知", impressFont));
            document.add(new Paragraph("本协议签订之日起至借款全部清偿之日止，甲方有义务在下列通信信息变更后三", pressFont));
            document.add(new Paragraph("日内提供更新后的信息给丙方和出借人（包含但不限于）：甲方本人、甲方的家", pressFont));
            document.add(new Paragraph("庭联系人及紧急联系人工作单位、居住地址、住所电话、手机号码、电子邮件等。", pressFont));
            document.add(new Paragraph("甲方确认知悉上述通讯信息对于甲方能否及时知晓协议履行相关事项的重要性，", pressFont));
            document.add(new Paragraph("因甲方不提供或不及时提供上述变更信息而导致丙方产生的损失，包括但不限于", pressFont));
            document.add(new Paragraph("调查费用、诉讼费用及律师费用及委托其他公民参加诉讼所产生的费用等支出及", pressFont));
            document.add(new Paragraph("其他损失（如有）将由甲方承担。关于本文件及《借款协议》履行及相关事宜的", pressFont));
            document.add(new Paragraph("通知，应当按照本文件页首载明的通讯信息发出，如果以特快专递或者挂号信形", pressFont));
            document.add(new Paragraph("式寄送的，自发出之日起第四日视为送达之日，其他方式通知的，比如电话、手", pressFont));
            document.add(new Paragraph("机短信、电子邮件、传真等，发出日即为送达日。", pressFont));
            
            document.add(new Paragraph("第十一条	其他", impressFont));
            document.add(new Paragraph("11.1 甲方无法按约定偿还本金及利息而产生逾期时，由乙方进行代偿，债务代偿", pressFont));
            document.add(new Paragraph("完毕后，原债权人不再对甲方依法享有该期到期债权，乙方成为甲方新的债权人，", pressFont));
            document.add(new Paragraph("可依法向甲方进行追偿。乙方应在欠款到期日的次日将全部所欠款项存入乙方在", pressFont));
            document.add(new Paragraph("存管银行预留的账号。", pressFont));
            document.add(new Paragraph("11.2 甲方还款方式", pressFont));
            document.add(new Paragraph("11.2.1 甲方按约履行还款义务的，甲方应在每期还款日将本期应偿还的本金、利", pressFont));
            document.add(new Paragraph("息、管理费用的全部款项存入甲方通过1号领投平台在存管银行开设的E账号内，", pressFont));
            document.add(new Paragraph("并通过1号领投平台（app、pc、微信端）进行还款操作。", pressFont));
            document.add(new Paragraph("11.2.2 甲方未按约定履行还款义务而产生逾期的，应由乙方于甲方发生逾期的次", pressFont));
            document.add(new Paragraph("日起先行垫付甲方当期逾期支付的欠款（包括但不限于本金、利息、罚息、违约", pressFont));
            document.add(new Paragraph("金、管理费、融资服务费），乙方垫付的全部欠款（包括但不限于本金、利息、", pressFont));
            document.add(new Paragraph("罚息、违约金、管理费、融资，服务费），为乙方重新借给甲方的借款本金，甲", pressFont));
            document.add(new Paragraph("方应按借款本金的月2%再向乙方支付利息。", pressFont));
            document.add(new Paragraph("	乙方完成垫付后，甲方需要通过1号领投平台偿还乙方所垫付的欠款，甲方", pressFont));
            document.add(new Paragraph("将所需偿还乙方的欠款总金额存入甲方通过1号领投平台在存管银行开设的E账", pressFont));
            document.add(new Paragraph("号内，并通过1号领投平台提供的相关还款功能进行还款。", pressFont));
            document.add(new Paragraph("11.3 如乙方未按本11.2条约定，未在欠款到期日次日履行全部代偿义务的，应", pressFont));
            document.add(new Paragraph("按照下述计算方式向丙方支付罚息。", pressFont));
            document.add(new Paragraph("罚息 = 剩余借款本金×罚息利率×逾期天数", pressFont));
            document.add(new Paragraph("11.4 甲乙丙三方签署本协议后，本协议于文首所载日期成立。自出借人将《借款", pressFont));
            document.add(new Paragraph("协议》所规定的借款本金数额，在扣除甲方应支付的各项费用后的剩余款项，支", pressFont));
            document.add(new Paragraph("付到甲方“1号领投”平台专用账号之日起生效。", pressFont));
            document.add(new Paragraph("11.5 本协议及其附件的任何修改、补充均须以书面形式作出。", pressFont));
            document.add(new Paragraph("11.6 本协议的传真件、复印件、扫描件等有效复本的效力与本协议原件效力一致。", pressFont));
            document.add(new Paragraph("11.7 甲乙丙三方均确认，本协议的签署、生效和履行以不违反中国的法律法规为", pressFont));
            document.add(new Paragraph("前提。如果本协议中的任何一条或多条违反适用的法律法规，则该条将被视为无", pressFont));
            document.add(new Paragraph("效，但该无效条款并不影响本协议其他条款的效力。", pressFont));
            document.add(new Paragraph("11.8 如果甲乙丙三方在本协议履行过程中发生任何争议，应友好协商解决；如协", pressFont));
            document.add(new Paragraph("商不成，三方均同意在丙方所在地人民法院提起诉讼。", pressFont));
            document.add(new Paragraph("11.9 本协议一式叁份，三方各执壹份，均具有同等法律效力。如有未尽事宜，可", pressFont));
            document.add(new Paragraph("签订补充协议予以约定。补充协议与本协议具有同等法律效力", pressFont));
            document.add(new Paragraph("（以下无正文）", pressFont));
            document.add(new Paragraph("（以下为签署页）", pressFont));
            document.add(new Paragraph("甲方（借款人、共同借款人）签字：", pressFont));
            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("乙方：荣耀时代（北京）信息咨询有限公司（签章）", pressFont));
            document.add(new Paragraph("法定代表人（签章）", pressFont));
            
            String filePath="/static/img/sign/rysd/rysd.png";
            filePath= NewLoanContractUtil.class. getClass().getResource("/").getPath() + filePath;
            // 定义一个图片
            Image jpeg = Image.getInstance(filePath);
            System.out.println("filePath=="+filePath);
            // 图片居中
            jpeg.setAlignment(Image.LEFT);
            jpeg.scaleAbsolute(113f, 113f);
            document.add(jpeg);
            
//            document.add(new Paragraph("    ", pressFont));
            document.add(new Paragraph("丙方：黑龙江荣耀天成投资有限公司（签章）", pressFont));
            document.add(new Paragraph("法定代表人（签章）", pressFont));
            
            String filePath2="/static/img/sign/rytc/rytc.png";
            filePath2= NewLoanContractUtil.class. getClass().getResource("/").getPath() + filePath2;
            // 定义一个图片
            Image jpeg2 = Image.getInstance(filePath2);
            
            System.out.println("filePath2=="+filePath2);
	
            // 图片居中
            jpeg2.setAlignment(Image.LEFT);
            jpeg2.scaleAbsolute(113f, 113f);
            document.add(jpeg2);
            
            
            return byteArrayOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            writer.close();
        }
        return null;
    }

    private static void setFooter(PdfWriter writer,String agreementNo) {
        //HeaderFooter headerFooter = new HeaderFooter(this);
        //更改事件，瞬间变身 第几页/共几页 模式。
        ContractFoot headerFooter = new ContractFoot();//就是上面那个类
        writer.setBoxSize("art", PageSize.A4);
        headerFooter.setHeader("借款服务协议/编号:"+agreementNo);
        writer.setPageEvent(headerFooter);
    }
}

