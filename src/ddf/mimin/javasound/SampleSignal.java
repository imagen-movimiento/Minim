package ddf.mimin.javasound;

import org.tritonus.share.sampled.FloatSampleBuffer;

import ddf.minim.AudioSignal;
import ddf.minim.Triggerable;

class SampleSignal implements AudioSignal, Triggerable
{
	private FloatSampleBuffer	buffer;
	private int[]					marks;
	private int						markAt;

	public SampleSignal(FloatSampleBuffer samps)
	{
		buffer = samps;
		marks = new int[20];
		for (int i = 0; i < marks.length; i++)
		{
			marks[i] = -1;
		}
		markAt = 0;
	}

	public void generate(float[] signal)
	{
		//	 build our signal from all the marks
      for (int i = 0; i < marks.length; i++)
      {
        int begin = marks[i];
        if (begin == -1) 
        {
      	  continue;
        }
        
        //JSMinim.debug("Sample trigger in process at marks[" + i + "] = " + marks[i]);
        int j, k;
        for (j = begin, k = 0; j < buffer.getSampleCount()
                            && k < signal.length; j++, k++)
        {
          signal[k] += buffer.getChannel(0)[j];
        }
        if ( j < buffer.getSampleCount() )
        {
          marks[i] = j;
        }
        else
        {
          //Minim.debug("Sample trigger ended.");
          marks[i] = -1;
        }
      }

	}

	public void generate(float[] left, float[] right)
	{
		//	 build our signal from all the marks
      for (int i = 0; i < marks.length; i++)
      {
        int begin = marks[i];
        if (begin == -1) 
        {
      	  continue;
        }
        
        //Minim.debug("Sample trigger in process at marks[" + i + "] = " + marks[i]);
        int j, k;
        for (j = begin, k = 0; j < buffer.getSampleCount()
                            && k < left.length; j++, k++)
        {
          left[k] += buffer.getChannel(0)[j];
          right[k] += buffer.getChannel(1)[k];
        }
        if ( j < buffer.getSampleCount() )
        {
          marks[i] = j;
        }
        else
        {
          //Minim.debug("Sample trigger ended.");
          marks[i] = -1;
        }
      }

	}

	public void trigger()
	{
		marks[markAt] = 0;
		markAt++;
		if (markAt == marks.length)
		{
			markAt = 0;
		}

	}

	public float[] getChannel(int channelNumber)
	{
		return buffer.getChannel(channelNumber);
	}
}