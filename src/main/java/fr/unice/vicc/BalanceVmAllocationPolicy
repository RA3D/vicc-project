
package fr.unice.vicc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class BalanceVmAllocationPolicy extends VmAllocationPolicy{
    private Map<String, Host> vmTable;
    
     public BalanceVmAllocationPolicy( List<? extends Host> list) {
        super(list);
        this.vmTable =  new HashMap<>();
    }
       @Override
    public void deallocateHostForVm(Vm v) {
        //get the host and remove the vm
        vmTable.get(v.getUid()).vmDestroy(v);
    }

    public static Object optimizeAllocation() {
        return null;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
        //Static scheduling, no migration, return null;
        return null;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        
        
        return true;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        // on test si le host peut supporter la vm si oui la fct retourn true
        if (host.vmCreate(vm)) {
            //the host is appropriate, we track it
            vmTable.put(vm.getUid(), host);
            return true;
        }
        return false;
    }

    @Override
      public Host getHost(Vm vm) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(vm.getUid());
    }

    @Override
    public Host getHost(int vmId, int userId) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(Vm.getUid(userId, vmId));
    }
}
