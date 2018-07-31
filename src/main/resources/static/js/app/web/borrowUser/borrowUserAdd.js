	var validator;
	var $borrowUserAddForm = $("#borrowUser-add-form");

	$(function() {
	    validateRule();

	    $("#borrowUser-add .btn-save").click(function() {
	        var name = $(this).attr("name");
	        var validator = $borrowUserAddForm.validate();
	        var flag = validator.form();
	        if (flag) {
	            if (name == "save") {
	                $.post(ctx + "borrowUser/add", $borrowUserAddForm.serialize(), function(r) {
	                    if (r.code == 0) {
	                        closeModal();
	                        $MB.n_success(r.msg);
	                        $MB.refreshTable("borrowUserTable");
	                    } else $MB.n_danger(r.msg);
	                });
	            }
	            if (name == "update") {
	                $.post(ctx + "borrowUser/update", $borrowUserAddForm.serialize(), function(r) {
	                    if (r.code == 0) {
	                        closeModal();
	                        $MB.n_success(r.msg);
	                        $MB.refreshTable("borrowUserTable");
	                    } else $MB.n_danger(r.msg);
	                });
	            }
	        }
	    });

	    $("#borrowUser-add .btn-close").click(function() {
	        closeModal();
	    });

	});

	function closeModal() {
		$("#borrowUser-add-button").attr("name", "save");
	    validator.resetForm();
	    $MB.closeAndRestModal("borrowUser-add");
	    $("#borrowUser-add-modal-title").html('新增借款人');
	}

	function validateRule() {
	    var icon = "<i class='zmdi zmdi-close-circle zmdi-hc-fw'></i> ";
	    validator = $borrowUserAddForm.validate({
	        rules: {
	            phone: {
	                required: true,
	                remote: {
	                    url: "borrowUser/checkPhone",
	                    type: "get",
	                    dataType: "json",
	                    data: {
	                    	phone: function() {
	                            return $("input[name='phone']").val().trim();
	                        },
	                        oldphone: function() {
	                            return $("input[name='oldphone']").val().trim();
	                        }
	                    }
	                }
	            },
	            userName: {
	                required: true
	            },
	            idNum: {
	                required: true
	            },
	            province: {
	                required: true
	            },
	            city: {
	                required: true
	            },
	            county: {
	                required: true
	            },
	            address: {
	                required: true
	            }
	        },
	        messages: {
	            phone: {
	                required: icon + "请输入手机号",
	                remote: icon + "该手机号已经存在"
	            }
	        }
	    });
	}
