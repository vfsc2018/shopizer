package com.salesmanager.core.business.repositories.voucherCode;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.voucherCode.VoucherCode;



public interface VoucherCodeRepository extends JpaRepository<VoucherCode, Long>,VoucherCodeRepositoryCustom {

}
