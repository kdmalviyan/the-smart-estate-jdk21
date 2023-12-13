package com.sfd.thesmartestate.multitenancy.tenants;

/**
 * @author kuldeep
 */
public class TenantContext {
    protected static final ThreadLocal<String> threadLocal = new InheritableThreadLocal<>() {
        @Override
        protected String initialValue() {
            try {
                return String.class.getDeclaredConstructor().newInstance();
            } catch (final Throwable e) {
                throw new RuntimeException(e);
            }
        }
        @Override
        protected String childValue(String parentValue) {
            return parentValue;
        }
    };

    public static void setTenantId(String tenantId) {
        threadLocal.set(tenantId);
    }

    public static String getTenantId() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.remove();
    }
}
