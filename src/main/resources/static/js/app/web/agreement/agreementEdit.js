function updateAgreement() {
    var selected = $("#agreementTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的协议！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个协议！');
        return;
    }
    var agreementId = selected[0].agreementId;
    $.post(ctx + "agreement/getAgreement", { "agreementId": agreementId }, function(r) {
        if (r.code == 0) {
            var $form = $('#agreement-add');
            $form.modal();
            var agreement = r.msg;
            $("#agreement-add-modal-title").html('修改协议');
            $form.find("input[name='agreementId']").val(agreement.agreementId);
            $form.find("#borrowUserId option[value='" + agreement.borrowUserId + "']").prop("selected",true);
            $form.find("#borrowUserId2 option[value='" + agreement.borrowUserId2 + "']").prop("selected",true);
            $form.find("input[name='repayOpenAcctName']").val(agreement.repayOpenAcctName);
            $form.find("input[name='repayAcctNo']").val(agreement.repayAcctNo);
            $form.find("input[name='repayOpenAcctBank']").val(agreement.repayOpenAcctBank);
            $form.find("input[name='repayCard']").val(agreement.repayCard);
            $form.find("input[name='signDate']").val(agreement.signDate);
            $form.find("input[name='approvalAmount']").val(agreement.approvalAmount);
            $form.find("input[name='generalRate']").val(agreement.generalRate);
            $form.find("#term option[value='" + agreement.term + "']").prop("selected",true);
            $form.find("input[name='financRate']").val(agreement.financRate);
            $form.find("input[name='borrowUse']").val(agreement.borrowUse);
            
            $("#agreement-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}