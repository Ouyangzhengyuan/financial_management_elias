package pers.elias.financial_management.component;

import org.springframework.stereotype.Component;

@Component
public class TableReload {
    private boolean isTableReload = false;

    public Boolean getIsTableReload() {
        return isTableReload;
    }

    public void setIsTableReload(boolean isTableReload) {
        this.isTableReload = isTableReload;
    }
}
