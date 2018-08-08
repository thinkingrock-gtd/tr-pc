/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can get a copy of the License at http://www.thinkingrock.com.au/cddl.html
 * or http://www.thinkingrock.com.au/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.thinkingrock.com.au/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * The Original Software is ThinkingRock. The Initial Developer of the Original
 * Software is Avente Pty Ltd, Australia.
 *
 * Portions Copyright 2006-2007 Avente Pty Ltd. All Rights Reserved.
 */
package au.com.trgtd.tr.data;

/**
 * Data access object (DAO) provider.
 *
 * @author Jeremy Moore
 * @param <T> The DAO type.
 */
public interface DAOProvider<T extends DAO> {

    /**
     * Provide the DAO.
     * @return the DAO.
     */
    public T provide();

    /**
     * Determines whether or not the DAO is initialized.
     * @return true if the DAO is initialized.
     */
    public boolean isInitialised();

    /**
     * Resets the DAO to an un-initialized state.
     */
    public void reset();

}
