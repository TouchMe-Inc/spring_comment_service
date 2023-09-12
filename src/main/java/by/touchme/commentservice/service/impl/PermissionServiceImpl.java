package by.touchme.commentservice.service.impl;

import by.touchme.commentservice.dto.AclEntity;
import by.touchme.commentservice.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class PermissionServiceImpl implements PermissionService {

    private final MutableAclService aclService;
    private final PlatformTransactionManager transactionManager;

    @Override
    public void addPermissionForUser(AclEntity targetObj, Permission permission, String username) {
        addPermissionForSid(targetObj, permission, new PrincipalSid(username));
    }

    @Override
    public void addPermissionForAuthority(AclEntity targetObj, Permission permission, String authority) {
        addPermissionForSid(targetObj, permission, new GrantedAuthoritySid(authority));
    }

    @Override
    public void addPermissionForSid(AclEntity targetObj, Permission permission, Sid sid) {
        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                ObjectIdentity oi = new ObjectIdentityImpl(targetObj.getClass(), targetObj.getId());
                MutableAcl acl;

                try {
                    acl = (MutableAcl) aclService.readAclById(oi);
                } catch (final NotFoundException nfe) {
                    acl = aclService.createAcl(oi);
                }

                acl.insertAce(acl.getEntries().size(), permission, sid, true);
                aclService.updateAcl(acl);
            }
        });
    }
}
