var validator;
var $agreementAddForm = $("#agreement-add-form");
var $borrowUserSelect = $("#borrowUserId");
var $borrowUserSelect2 = $("#borrowUserId2");

$(function() {
    validateRule();
    initBorrowUser();//初始化借款人
    
    $("#agreement-add .btn-save").click(function() {
        var name = $(this).attr("name");
        var validator = $agreementAddForm.validate();
        var flag = validator.form();
        if (flag) {
            if (name == "save") {
                $.post(ctx + "agreement/add", $agreementAddForm.serialize(), function(r) {
                    if (r.code == 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("agreementTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name == "update") {
                $.post(ctx + "agreement/update", $agreementAddForm.serialize(), function(r) {
                    if (r.code == 0) {
                        closeModal();
                        $MB.n_success(r.msg);
                        $MB.refreshTable("agreementTable");
                    } else $MB.n_danger(r.msg);
                });
            }
            if (name == "cre") {
            	$.post(ctx + "agreement/creAgreement", $agreementAddForm.serialize(), function(r) {
        			if (r.code == 0) {
        				closeModal();
        				$MB.n_success(r.msg);
        				$MB.refreshTable("agreementTable");
        			} else $MB.n_danger(r.msg);
        		});
            }
        }
    });
    $("#agreement-add .btn-close").click(function() {
        closeModal();
    });
   /* $MB.calenders('input[name="signDate"]',false,false);*/
    
});

function closeModal() {
	$("#agreement-add-button").attr("name", "save");
    validator.resetForm();
    $MB.closeAndRestModal("agreement-add");
    $("#agreement-add-modal-title").html('新增协议');
}

function validateRule() {
    var icon = "<i class='zmdi zmdi-close-circle zmdi-hc-fw'></i> ";
    validator = $agreementAddForm.validate({
        rules: {
            repayOpenAcctName: {
                required: true
            },
            repayAcctNo: {
                required: true,
                number:true
            },
            repayOpenAcctBank: {
                required: true
            },
            repayCard: {
                required: true
            },
            signDate: {
                required: true
            },
            approvalAmount: {
                required: true,
                number:true
            },
            generalRate: {
                required: true,
                number:true
            },
            financRate: {
                required: true,
                number:true
            },
            borrowUse: {
                required: true
            }
        },
        messages: {
            
        }
    });
}
function initBorrowUser() {
    $.post(ctx + "borrowUser/list", function(r) {
        var data = r.rows;
        console.log(data);
        var option = "";
        var option2 = "";
        for (var i = 0; i < data.length; i++) {
        	option += "<option value='" + data[i].borrowUserId + "'>" + data[i].userName+"&nbsp;"+data[i].phone + "</option>";
        	option2 += "<option value='" + data[i].borrowUserId + "'>" + data[i].userName+"&nbsp;"+data[i].phone + "</option>";
        }
        $borrowUserSelect.html("<option value=''>--请选择--</option>").append(option);
        $borrowUserSelect2.html("<option value=''>--请选择--</option>").append(option2);
    });
}