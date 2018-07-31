package com.rytc.web.service;

import java.util.List;

import com.rytc.common.service.IService;
import com.rytc.web.domain.Agreement;

public interface AgreementService extends IService<Agreement>{

	List<Agreement> findAllAgreement(Agreement agreement);

	void addAgreement(Agreement agreement);

	void deleteAgreement(List<String> list);

	void updateAgreement(Agreement agreement);

	Agreement findById(Integer agreementId);

	void onSubmit(Integer agreementId);

	void creAgreement(Agreement agreement) throws Exception;

}
