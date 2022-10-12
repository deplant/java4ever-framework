package tech.deplant.java4ever.framework.contract;

import tech.deplant.java4ever.binding.EverSdkException;
import tech.deplant.java4ever.framework.Address;

import java.math.BigInteger;

/**
 * Giver interface is used in various deploy methods.
 * Its give() method is called to send funds before contracts deploy.
 * By replacing Giver implementations you can flawlessly replace EverOS Giver
 * with multisigs or all your own giver contracts.
 */
public interface Giver {

	/**
	 * Implementations of this method should send specified amount to
	 * specified address.
	 *
	 * @param to     Everscale address where to send funds
	 * @param amount Amount to send (in nanoevers)
	 * @throws EverSdkException
	 */
	public void give(Address to, BigInteger amount) throws EverSdkException;
}
