package com.salesmanager.core.business.repositories.vouchercode;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.vouchercode.VoucherCode;



public interface VoucherCodeRepository extends JpaRepository<VoucherCode, Long>,VoucherCodeRepositoryCustom {

}
